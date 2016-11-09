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

public class ActionShallowHistory extends ShallowHistoryFunction<AtomicEvent> 
{
	public ActionShallowHistory(Automaton a, int size) 
	{
		super(a, size);
	}

	@Override
	public MathSet<MathList<AtomicEvent>> processTransition(Edge<AtomicEvent> edge)
	{
		m_window.add(edge.getLabel());
		MathSet<MathList<AtomicEvent>> out = new MathSet<MathList<AtomicEvent>>();
		MathList<AtomicEvent> new_list = new MathList<AtomicEvent>();
		new_list.addAll(m_window);
		out.add(new_list);
		return out;
	}

	@Override
	public MathSet<MathList<AtomicEvent>> getStartClass()
	{
		MathSet<MathList<AtomicEvent>> out = new MathSet<MathList<AtomicEvent>>();
		MathList<AtomicEvent> new_list = new MathList<AtomicEvent>();
		out.add(new_list);
		return out;
	}
}
