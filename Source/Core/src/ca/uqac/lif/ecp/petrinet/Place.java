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
package ca.uqac.lif.ecp.petrinet;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.ecp.Event;

/**
 * A place in a Petri net is a graph node that can contain zero or more
 * <em>tokens</em>. It is linked via incoming and outgoing arrows to
 * transitions.
 * @author Sylvain Hallé
 *
 */
public class Place<T extends Event>
{
	protected Set<Transition<T>> m_incoming;
	protected Set<Transition<T>> m_outgoing;
	protected int m_marking = 0;
	protected String m_label;

	
	public Place()
	{
		super();
		m_label = "";
		m_incoming = new HashSet<Transition<T>>();
		m_outgoing = new HashSet<Transition<T>>();
	}
	
	public Place(String label)
	{
		this();
		assert label != null;
		m_label = label;
	}
	
	public void addIncomingTransition(Transition<T> t)
	{
		m_incoming.add(t);
	}
	
	public void addOutgoingTransition(Transition<T> t)
	{
		m_outgoing.add(t);
	}
	
	public void setMarking(int value)
	{
		m_marking = value;
	}
	
	public void put()
	{
		m_marking++;
	}
	
	public void consume()
	{
		m_marking--;
	}
	
	public boolean isEmpty()
	{
		return m_marking == 0;
	}
	
	@Override
	public int hashCode()
	{
		return m_label.hashCode();  
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof Place))
			return false;
		return equals((Place<T>) o);
	}
	
	public boolean equals(Place<T> p)
	{
		if (p == null)
			return false;
		return m_label.compareTo(p.m_label) == 0;
	}
}