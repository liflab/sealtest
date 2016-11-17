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

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

/**
 * Ground term that asserts that the current event is equal
 * to the event associated to this term
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class Atom<T extends Event> extends Operator<T>
{
	/**
	 * The event that this atom asserts
	 */
	protected final T m_event;
	
	/**
	 * The event we've seen when we evaluated the atom
	 */
	protected EventLeaf<T> m_eventSeen = null;
		
	public Atom(T e)
	{
		super();
		m_event = e;
	}

	@Override
	public void evaluate(T event) 
	{
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
		return a.m_event.equals(m_event);
	}
	
	@Override
	public Atom<T> copy(boolean with_tree)
	{
		Atom<T> a = new Atom<T>(m_event);
		if (with_tree == true)
		{
			a.m_eventSeen = m_eventSeen;
		}
		return a;
	}

	@Override
	public void acceptPrefix(HologramVisitor<T> visitor)
	{
		visitor.visit(this);
		m_eventSeen.acceptPrefix(visitor);
		visitor.backtrack();
	}
	
	@Override
	public String getRootSymbol()
	{
		return m_event.toString();
	}

	@Override
	public int size(boolean with_tree) 
	{
		return 1;
	}

	@Override
	public List<Operator<T>> getTreeChildren() 
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		if (m_eventSeen != null)
		{
			list.add(m_eventSeen);
		}
		return list;
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		if (m_eventSeen != null)
		{
			m_eventSeen.delete();			
		}
	}
	
	@Override
	public void addOperand(Operator<T> op)
	{
		// Nothing to do
	}
}
