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
package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.structures.MathList;
import ca.uqac.lif.structures.MathSet;

public class TransitionShallowHistory extends ShallowHistoryFunction<Edge<AtomicEvent>> 
{
	public TransitionShallowHistory(Automaton a, int size) 
	{
		super(a, size);
	}

	@Override
	public MathSet<MathList<Edge<AtomicEvent>>> processTransition(Edge<AtomicEvent> edge)
	{
		m_window.add(edge);
		MathSet<MathList<Edge<AtomicEvent>>> out = new MathSet<MathList<Edge<AtomicEvent>>>();
		MathList<Edge<AtomicEvent>> new_list = new MathList<Edge<AtomicEvent>>();
		new_list.addAll(m_window);
		out.add(new_list);
		return out;
	}

	@Override
	public MathSet<MathList<Edge<AtomicEvent>>> getStartClass()
	{
		MathSet<MathList<Edge<AtomicEvent>>> out = new MathSet<MathList<Edge<AtomicEvent>>>();
		MathList<Edge<AtomicEvent>> new_list = new MathList<Edge<AtomicEvent>>();
		out.add(new_list);
		return out;
	}
	
	@Override
	public String toString()
	{
		String out = "Transition history of depth " + m_window.m_size;
		if (!m_automaton.toString().isEmpty())
		{
			out += " on " + m_automaton.getTitle();
		}
		return out;
	}
}
