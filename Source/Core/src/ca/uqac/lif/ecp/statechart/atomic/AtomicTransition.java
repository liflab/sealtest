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
package ca.uqac.lif.ecp.statechart.atomic;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.ecp.statechart.Action;
import ca.uqac.lif.ecp.statechart.Configuration;
import ca.uqac.lif.ecp.statechart.Statechart;
import ca.uqac.lif.ecp.statechart.Transition;

/**
 * Transition of a statechart where the label corresponds to an atomic event
 * @author Sylvain Hallé
 */
public class AtomicTransition extends Transition<AtomicEvent> 
{
	/**
	 * The event this transitions is supposed to match
	 */
	protected AtomicEvent m_event;
		
	/**
	 * Creates a new atomic transition
	 * @param e The event associated to this transition
	 */
	public AtomicTransition(AtomicEvent e)
	{
		super();
		m_event = e;
	}
	
	public AtomicTransition(AtomicEvent e, Configuration<AtomicEvent> target)
	{
		super();
		m_event = e;
		setTarget(target);
	}
	
	public AtomicTransition(AtomicEvent e, Operator<AtomicEvent> guard, Action<AtomicEvent> action, Configuration<AtomicEvent> target)
	{
		super();
		m_event = e;
		m_action = action;
		m_guard = guard;
		setTarget(target);
	}

	@Override
	public int hashCode()
	{
		return m_event.hashCode() * m_target.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof AtomicTransition))
		{
			return false;
		}
		AtomicTransition at = (AtomicTransition) o;
		if (!at.m_event.equals(m_event))
		{
			return false;
		}
		return hasSameParameters(at);
	}
	
	/**
	 * Sets the target of this transition
	 * @param target The target
	 */
	public void setTarget(Configuration<AtomicEvent> target)
	{
		m_target = target;
	}
	
	/**
	 * Gets the event associated to this transition
	 * @return The event
	 */
	public AtomicEvent getEvent()
	{
		return m_event;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_event);
		if (m_guard != null)
		{
			out.append(" [").append(m_guard).append("]");
		}
		if (m_action != null)
		{
			out.append(" /").append(m_action);
		}
		out.append(" -> ").append(m_target);
		return out.toString();
	}

	@Override
	public AtomicTransition clone()
	{
		if (m_guard != null)
		{
			return new AtomicTransition(m_event, m_guard.copy(), m_action, m_target);
		}
		return new AtomicTransition(m_event, null, m_action, m_target);
	}
	
	@Override
	protected boolean afterGuard(AtomicEvent event, Statechart<AtomicEvent> statechart)
	{
		return m_event.equals(event);
	}
}
