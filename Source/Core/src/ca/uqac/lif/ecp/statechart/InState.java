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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.EventLeaf;
import ca.uqac.lif.ecp.ltl.HologramVisitor;
import ca.uqac.lif.ecp.ltl.Operator;

/**
 * Predicate that asserts that the statechart is in a particular state.
 * @author Sylvain Hallé
 *
 * @param <T> The type of events in the statechart
 */
public class InState<T extends Event> extends Operator<T> 
{
	/**
	 * The name of the state to be queried for
	 */
	protected final String m_stateName;
	
	/**
	 * The statechart whose state is asserted
	 */
	protected final Statechart<T> m_statechart;
	
	/**
	 * The event seen
	 */
	protected EventLeaf<T> m_eventSeen;
	
	/**
	 * Creates a new state predicate
	 * @param statechart The statechart whose state is asserted
	 * @param name The name of the state to be queried for
	 */
	public InState(Statechart<T> statechart, String name)
	{
		super();
		m_statechart = statechart;
		m_stateName = name;
		m_eventSeen = null;
	}

	@Override
	public Operator<T> copy(boolean with_tree) 
	{
		return new InState<T>(m_statechart, m_stateName);
	}

	@Override
	public void evaluate(T event)
	{
		if (event == null)
		{
			return;
		}
		if (m_eventSeen == null)
		{
			m_eventSeen = new EventLeaf<T>(event);
			// Move up to topmost parent
			Statechart<T> top_parent = m_statechart;
			Statechart<T> cur_chart = m_statechart;
			do
			{
				top_parent = cur_chart;
				cur_chart = cur_chart.getParent();
			} while (cur_chart != null);
			// Get state of this parent
			Configuration<T> conf = top_parent.getCurrentConfiguration();
			if (conf.contains(m_stateName))
			{
				m_value = Value.TRUE;
				return;
			}
			m_value = Value.FALSE;
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size(boolean with_tree) 
	{
		return 1;
	}

	@Override
	public String getRootSymbol() 
	{
		return "InState";
	}

	@Override
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}

	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}

	@Override
	public List<Operator<T>> getTreeChildren() 
	{
		return new ArrayList<Operator<T>>();
	}

	@Override
	public void addOperand(Operator<T> op)
	{
		// Nothing to do
	}

	@Override
	public void clean() 
	{
		m_eventSeen = null;
		m_value = Value.INCONCLUSIVE;
	}
	
	@Override
	public String toString()
	{
		return "In State " + m_stateName;
	}

}
