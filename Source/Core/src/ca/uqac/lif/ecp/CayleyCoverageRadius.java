/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyCoverageRadius.RadiusMap;
import ca.uqac.lif.structures.MathMap;
import ca.uqac.lif.structures.MathSet;

public class CayleyCoverageRadius<T extends Event,U> extends CayleyCoverageMetric<T,U,RadiusMap> 
{
	/**
	 * Whether the coverage is weighted by the cardinality of the
	 * equivalence classes
	 */
	boolean m_weighted = false;
	
	public CayleyCoverageRadius(CayleyGraph<T, U> graph, TriagingFunction<T, U> function) 
	{
		super(graph, function);
	}
	
	/**
	 * Determines whether the coverage is weighted by the cardinality of the
	 * equivalence classes
	 * @param b Set to <code>true</code> to weight by cardinality
	 */
	public void setWeighted(boolean b)
	{
		m_weighted = b;
	}
	
	/**
	 * A map from trace length &#x2113; to a rational number between 0 and 1.
	 * This number indicates how much coverage is achieved for equivalence
	 * classes reachable by a trace of length &#x2113; or less.
	 */
	public static class RadiusMap extends MathMap<Integer,Float>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * A title to give to this plot
		 */
		protected String m_title;
		
		protected final boolean m_weighted;
		
		public RadiusMap(boolean weighted)
		{
			this("", weighted);
		}
		
		public RadiusMap(String title, boolean weighted)
		{
			super();
			m_title = title;
			m_weighted = weighted;
		}
		
		/**
		 * Produces a Gnuplot rendition of the radius map
		 * @return A Gnuplot string
		 */
		public String toGnuplot()
		{
			StringBuilder out = new StringBuilder();
			String title = "Coverage radius of " + m_title;
			if (m_weighted)
			{
				title = "Weighted coverage radius of " + m_title;
			}
			out.append("set title \"").append(title).append("\"\n");
			out.append("set terminal png\n");
			out.append("set datafile separator \",\"\n");
			out.append("set yrange [0:1]\n");
			out.append("set border 3\n");
			out.append("unset key\n");
			out.append("set xlabel \"Trace length\"\n");
			out.append("set ylabel \"Class coverage\"\n");
			out.append("plot '-' using 1:2 with lines\n\n");
			List<Integer> values = new ArrayList<Integer>();
			values.addAll(keySet());
			Collections.sort(values);
			int last_x = -1;
			float last_y = -1;
			for (Integer cur_x : values)
			{
				float cur_y = get(cur_x);
				if (last_x != -1)
				{
					out.append(cur_x).append(",").append(last_y).append("\n");
				}
				out.append(cur_x).append(",").append(cur_y).append("\n");
				last_x = cur_x;
				last_y = cur_y;
			}
			out.append("end\n");
			return out.toString();
		}
	}

	@Override
	public RadiusMap getCoverage(Set<Trace<T>> traces) 
	{
		RadiusMap map = new RadiusMap(m_function.toString(), m_weighted);
		Map<Integer,Set<MathSet<U>>> classes_by_depth = m_graph.getClassesByDepth();
		Set<MathSet<U>> covered_classes = getCoveredClasses(traces, true);
		Set<Integer> depths = classes_by_depth.keySet();
		int num_depths = depths.size();
		int class_count = 0;
		int total_count = 0;
		for (int depth = 0, depth_count = 0; depth_count < num_depths; depth++)
		{
			Map<MathSet<U>,Integer> num_traces = null;
			if (m_weighted)
			{
				// Don't compute that map unless we need it
				num_traces = m_graph.getClassCardinality(depth + 1, false);
			}
			if (classes_by_depth.containsKey(depth))
			{
				depth_count++;
				Set<MathSet<U>> classes = classes_by_depth.get(depth);
				for (MathSet<U> clazz : classes)
				{
					if (m_weighted)
					{
						total_count += num_traces.get(clazz);
					}
					else
					{
						total_count += 1;
					}
					if (covered_classes.contains(clazz))
					{
						if (m_weighted)
						{
							class_count += num_traces.get(clazz);
						}
						else
						{
							class_count += 1;
						}
					}
				}
			}
			if (total_count > 0)
			{
				map.put(depth, (float) class_count / total_count);
			}
			else
			{
				map.put(depth, 0f);
			}
		}
		return map;
	}
}
