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
	 * Up to what depth to compute the radius
	 */
	protected int m_depth;

	public CayleyCoverageRadius(CayleyGraph<T, U> graph, TriagingFunction<T, U> function) 
	{
		super(graph, function);
		setDepth(graph.getDepth());
	}
	
	public CayleyCoverageRadius(CayleyGraph<T, U> graph, TriagingFunction<T, U> function, int depth) 
	{
		super(graph, function);
		setDepth(depth);
	}
	
	public void setDepth(int depth)
	{
		m_depth = depth;
	}

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
		
		public RadiusMap()
		{
			this("");
		}
		
		public RadiusMap(String title)
		{
			super();
			m_title = title;
		}
		
		/**
		 * Produces a Gnuplot rendition of the radius map
		 * @return A Gnuplot string
		 */
		public String toGnuplot()
		{
			StringBuilder out = new StringBuilder();
			out.append("set title \"Coverage radius of ").append(m_title).append("\"\n");
			out.append("set terminal png\n");
			out.append("set datafile separator \",\"\n");
			out.append("set yrange [0:1]\n");
			out.append("set border 3\n");
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
		RadiusMap map = new RadiusMap(m_function.toString());
		Map<Integer,Set<MathSet<U>>> classes_by_depth = m_graph.getClassesByDepth(m_depth);
		Set<MathSet<U>> covered_classes = getCoveredClasses(traces, true);
		int class_count = 0;
		int total_count = 0;
		for (int depth = 0; depth < m_depth; depth++)
		{
			Set<MathSet<U>> classes = classes_by_depth.get(depth);
			total_count += classes.size();
			for (MathSet<U> clazz : classes)
			{
				if (covered_classes.contains(clazz))
				{
					class_count++;
				}
			}
			map.put(depth, (float) class_count / total_count);
		}
		return map;
	}
}
