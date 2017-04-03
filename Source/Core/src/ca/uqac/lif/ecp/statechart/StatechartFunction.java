/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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
package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.UnexpectedError;
import ca.uqac.lif.structures.MathSet;

/**
 * Triaging function based on a UML statechart
 */
public abstract class StatechartFunction<T extends Event,U extends Object> extends TriagingFunction<T,U>
{
	/**
	 * The statechart this function uses as its reference
	 */
	protected Statechart<T> m_statechart;
	
	/**
	 * The current vertex in the automaton after reading the previous
	 * events
	 */
	protected Configuration<T> m_currentVertex;
	
	/**
	 * Creates a new triaging function from an automaton
	 * @param a The statechart this function uses as its reference
	 */
	public StatechartFunction(Statechart<T> a)
	{
		super();
		m_statechart = a;
		m_statechart.reset();
	}
	
	@Override
	public void reset()
	{
		m_statechart.reset();
	}
	
	@Override
	public MathSet<U> read(T e)
	{
		Configuration<T> start_state = m_statechart.getCurrentConfiguration();
		Transition<T> edge = m_statechart.takeTransition(e);
		if (edge == null)
		{
			throw new UnexpectedError("The transition relation of the statechart is not total: no outgoing edge from "
					+ m_currentVertex + " with event " + e);
		}
		return processTransition(start_state, edge);
	}
	
	public abstract MathSet<U> processTransition(Configuration<T> start_state, Transition<T> edge);
}
