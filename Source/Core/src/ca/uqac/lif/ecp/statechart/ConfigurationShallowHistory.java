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
import ca.uqac.lif.ecp.statechart.ShallowHistoryFunction;
import ca.uqac.lif.structures.MathList;
import ca.uqac.lif.structures.MathSet;

/**
 * Triaging function that associates a trace to the list of the
 * last <i>n</i> configurations of the statechart. A configuration
 * includes all the state pointers in the statechart, as well as
 * the values of all its defined state variables.
 * @author Sylvain Hallé
 *
 * @param <T> The type of events in the statechart
 */
public abstract class ConfigurationShallowHistory<T extends Event> extends ShallowHistoryFunction<T,Configuration<T>> 
{
	public ConfigurationShallowHistory(Statechart<T> a, int size) 
	{
		super(a, size);
	}
	
	public ConfigurationShallowHistory(Statechart<T> a)
	{
		this(a, 1);
	}

	@Override
	public MathSet<MathList<Configuration<T>>> processTransition(Configuration<T> start_state, Transition<T> edge)
	{
		// This is the first event: add both source and destination
		// to the window
		if (m_window.isEmpty())
		{
			m_window.add(start_state);
		}
		m_window.add(m_statechart.getCurrentConfiguration());
		MathSet<MathList<Configuration<T>>> out = new MathSet<MathList<Configuration<T>>>();
		MathList<Configuration<T>> new_list = new MathList<Configuration<T>>();
		new_list.addAll(m_window);
		out.add(new_list);
		return out;
	}

	@Override
	public MathSet<MathList<Configuration<T>>> getStartClass()
	{
		MathSet<MathList<Configuration<T>>> out = new MathSet<MathList<Configuration<T>>>();
		MathList<Configuration<T>> new_list = new MathList<Configuration<T>>();
		new_list.add(m_statechart.getInitialConfiguration());
		out.add(new_list);
		return out;
	}
	
	@Override
	public String toString()
	{
		String out = "State history of depth " + m_window.m_size;
		if (!m_statechart.toString().isEmpty())
		{
			out += " on a statechart";
		}
		return out;
	}
}
