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

import ca.uqac.lif.ecp.atomic.AtomicEvent;

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
	 * The target of this transition
	 */
	protected StateNode<AtomicEvent> m_target;
	
	/**
	 * Creates a new atomic transition
	 * @param e The event associated to this transition
	 */
	public AtomicTransition(AtomicEvent e)
	{
		super();
		m_event = e;
	}
	
	public AtomicTransition(AtomicEvent e, StateNode<AtomicEvent> target)
	{
		super();
		m_event = e;
		setTarget(target);
	}

	@Override
	public boolean matches(AtomicEvent event) 
	{
		return m_event.equals(event);
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
		return at.m_target.equals(m_target);
	}
	
	/**
	 * Sets the target of this transition
	 * @param target The target
	 */
	public void setTarget(StateNode<AtomicEvent> target)
	{
		m_target = target;
	}
	
	public AtomicEvent getEvent()
	{
		return m_event;
	}

	@Override
	public StateNode<AtomicEvent> getTarget() 
	{
		return m_target;
	}
	
	@Override
	public String toString()
	{
		return m_event + " -> " + m_target;
	}

	@Override
	public AtomicTransition clone()
	{
		AtomicTransition at = new AtomicTransition(m_event);
		at.setTarget(m_target);
		return at;
	}
}
