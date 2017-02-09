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
package ca.uqac.lif.ecp.atomic.petrinet;

import java.util.HashSet;
import java.util.Set;

/**
 * A place in a Petri net is a graph node that can contain zero or more
 * <em>tokens</em>. It is linked via incoming and outgoing arrows to
 * transitions.
 * @author Sylvain Hallé
 *
 */
public class Place
{
	protected Set<Transition> m_incoming;
	protected Set<Transition> m_outgoing;
	protected int m_marking = 0;
	protected String m_label;

	
	public Place()
	{
		super();
		m_label = "";
		m_incoming = new HashSet<Transition>();
		m_outgoing = new HashSet<Transition>();
	}
	
	public Place(String label)
	{
		this();
		assert label != null;
		m_label = label;
	}
	
	public void addIncomingTransition(Transition t)
	{
		m_incoming.add(t);
	}
	
	public void addOutgoingTransition(Transition t)
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
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof Place))
			return false;
		return equals((Place) o);
	}
	
	public boolean equals(Place p)
	{
		if (p == null)
			return false;
		return m_label.compareTo(p.m_label) == 0;
	}
}