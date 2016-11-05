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

import ca.uqac.lif.structures.MathList;
import ca.uqac.lif.structures.MathSet;

/**
 * Triaging function that returns the number of t-way sequences 
 * contained in the trace
 * @author Sylvain Hallé
 *
 */
public class TWayEventSequenceFunction<T extends Event> extends TriagingFunction<T,MathList<T>>
{
	/**
	 * The trace read so far
	 */
	protected Trace<T> m_eventsRead;
	
	/**
	 * The strength of the test suite (i.e. the <i>t</i> in
	 * <i>t</i>-way)
	 */
	protected int m_strength;

	public TWayEventSequenceFunction(int strength)
	{
		super();
		m_eventsRead = new Trace<T>();
		m_strength = strength;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_eventsRead.clear();
	}

	@Override
	public MathSet<MathList<T>> read(T event)
	{
		// TODO: there is surely a way to incrementally compute the
		// sequences instead of recomputing from scratch at each new event
		// (let's not care for now)
		m_eventsRead.add(event);
		return getCombinations(m_eventsRead);
	}
	
	/**
	 * Gets the combinations of events contained in the current trace
	 * @param trace The trace
	 * @return The set of combinations
	 */
	protected MathSet<MathList<T>> getCombinations(Trace<T> trace)
	{
		return getCombinations(trace, new MathList<T>(), 0, Math.min(trace.size(), m_strength));
	}
	
	/**
	 * Gets the combinations of events contained in the current trace
	 * @param trace The trace
	 * @param current The current prefix to be considered
	 * @param n The number of events remaining to put
	 * @return The set of combinations
	 */
	protected MathSet<MathList<T>> getCombinations(Trace<T> trace, MathList<T> current, int j, int n)
	{
		MathSet<MathList<T>> set = new MathSet<MathList<T>>();
		if (n == 0)
		{
			set.add(current);
			return set;
		}
		for (int i = j; i < trace.size(); i++)
		{
			MathList<T> new_current = new MathList<T>();
			new_current.addAll(current);
			new_current.add(trace.get(i));
			set.addAll(getCombinations(trace, new_current, i + 1, n-1));
		}
		return set;
	}

	@Override
	public MathSet<MathList<T>> getStartClass() 
	{
		return new MathSet<MathList<T>>();
	}

}