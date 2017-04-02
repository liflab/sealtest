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
package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

/**
 * Ground term that asserts that the current event is equal
 * to the event associated to this term
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class Atom<T extends Event> extends GroundTerm<T>
{
	/**
	 * The event that this atom asserts
	 */
	protected final T m_event;
			
	public Atom(T e)
	{
		super();
		m_event = e;
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
			if (event.equals(m_event))
			{
				m_value = Value.TRUE;
				return;
			}
			m_value = Value.FALSE;
		}
	}
	
	/**
	 * Gets the event that this atom asserts
	 * @return The event
	 */
	public T getEvent()
	{
		return m_event;
	}
	
	@Override
	public String toString()
	{
		return m_event.toString();
	}
	
	@Override
	public int hashCode()
	{
		return m_event.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Atom<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		@SuppressWarnings("unchecked")
		Atom<T> a = (Atom<T>) o;
		if (m_value != a.m_value)
		{
			return false;
		}
		return a.m_event.equals(m_event) && (m_eventSeen.isDeleted() || m_eventSeen.equals(a.m_eventSeen));
	}
	
	@Override
	public Atom<T> copy(boolean with_tree)
	{
		Atom<T> a = new Atom<T>(m_event);
		super.copyInto(a, with_tree);
		if (with_tree == true)
		{
			if (m_eventSeen != null)
			{
				a.m_eventSeen = m_eventSeen.copy(with_tree);
			}
		}
		return a;
	}

	@Override
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree)
	{
		visitor.visit(this);
		if (in_tree)
		{
			if (m_eventSeen != null)
			{
				m_eventSeen.acceptPrefix(visitor, in_tree);
			}
		}
		visitor.backtrack();
	}
	
	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree)
	{
		if (in_tree)
		{
			m_eventSeen.acceptPostfix(visitor, in_tree);
		}
		visitor.visit(this);
		visitor.backtrack();
	}
	
	@Override
	public String getRootSymbol()
	{
		return m_event.toString();
	}
}
