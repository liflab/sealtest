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

import ca.uqac.lif.structures.MathSet;

/**
 * Coverage metric based on a Cayley graph
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 * @param <U> The type of the categories in the Cayley graph
 * @param <V> The return type of the coverage metric
 */
public abstract class CayleyCoverageMetric<T extends Event,U extends Object,V> extends CoverageMetric<T,V> 
{
	/**
	 * The Cayley Graph from which the coverage will be computed
	 */
	protected CayleyGraph<T,U> m_graph;

	/**
	 * The triaging function used to classify traces
	 */
	protected TriagingFunction<T,U> m_function;

	public CayleyCoverageMetric(CayleyGraph<T,U> graph, TriagingFunction<T,U> function)
	{
		super();
		m_graph = graph;
		m_function = function;
	}

	/**
	 * Returns the set of all the equivalences classes covered by a set
	 * of traces
	 * @param traces The set of traces
	 * @param prefix_closed Set to true to include classes covered by
	 *   all prefixes of each trace 
	 * @return The set of classes
	 */
	public Set<MathSet<U>> getCoveredClasses(Set<Trace<T>> traces, boolean prefix_closed)
	{
		Set<MathSet<U>> covered_classes = new HashSet<MathSet<U>>();
		for (Trace<T> trace : traces)
		{
			covered_classes.addAll(getCoveredClasses(trace, prefix_closed));
		}
		return covered_classes;
	}

	/**
	 * Returns the set of all the equivalences classes covered by a 
	 * traces and all its prefixes
	 * @param traces The set of traces
	 * @param prefix_closed Set to true to include classes covered by
	 *   all prefixes of each trace 
	 * @return The set of classes
	 */
	public Set<MathSet<U>> getCoveredClasses(Trace<T> trace, boolean prefix_closed)
	{
		Set<MathSet<U>> covered_classes = new HashSet<MathSet<U>>();
		m_function.reset();
		if (prefix_closed)
		{
			covered_classes.add(m_function.getStartClass());
			for (T event : trace)
			{
				MathSet<U> clazz = m_function.read(event);
				covered_classes.add(clazz);
			}
		}
		else
		{
			MathSet<U> last = m_function.getStartClass();
			for (T event : trace)
			{
				MathSet<U> clazz = m_function.read(event);
				last = clazz;
			}
			covered_classes.add(last);
		}
		return covered_classes;
	}
}
