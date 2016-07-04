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

import java.util.HashSet;
import java.util.Set;

/**
 * Coverage of a set of traces based on the fraction of all equivalence classes
 * of the triaging function that are present in one of the prefixes of some
 * trace.
 * @author Sylvain
 *
 * @param <T> The type of events in the trace
 * @param <U> The output type of the triaging function
 */
public class CayleyEquivalenceCoverage<T extends Event,U extends Object> extends CayleyCoverageMetric<T,U,Float> 
{
	public CayleyEquivalenceCoverage(CayleyGraph<T, U> graph, TriagingFunction<T,U> function) 
	{
		super(graph, function);
	}

	@Override
	public Float getCoverage(Set<Trace<T>> traces) 
	{
		float nb_total_classes = m_graph.getLabelling().values().size();
		if (nb_total_classes == 0)
		{
			return 0f;
		}
		Set<U> covered_classes = new HashSet<U>();
		for (Trace<T> trace : traces)
		{
			m_function.reset();
			for (T event : trace)
			{
				U category = m_function.read(event);
				covered_classes.add(category);
			}
		}
		return (float) covered_classes.size() / nb_total_classes;
	}

}
