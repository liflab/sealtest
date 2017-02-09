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

import ca.uqac.lif.ecp.Event;

/**
 * A transition in a Petri net is a graph node that is linked via
 * incoming and outgoing arrows to places.
 * A transition of a Petri net may <em>fire</em> whenever there are sufficient
 * tokens in the places from all incoming arcs; when it fires, it consumes
 * these tokens, and places tokens at the end of all output arcs.
 * @author Sylvain Hallé
 *
 */
public class Transition<T extends Event>
{
	protected T m_label;
	protected Set<Place> m_incoming;
	protected Set<Place> m_outgoing;
	
	public Transition()
	{
		super();
		m_label = null;
		m_incoming = new HashSet<Place>();
		m_outgoing = new HashSet<Place>();
	}
	
	public Transition(T event)
	{
		this();
		m_label = event;
	}
	
	public void addIncomingPlace(Place p)
	{
		m_incoming.add(p);
	}
	
	public void addOutgoingPlace(Place p)
	{
		m_outgoing.add(p);
	}
	
	public boolean isEnabled()
	{
		for (Place p : m_incoming)
			if (p.isEmpty())
				return false;
		return true;
	}
	
	public void fire()
	{
		assert isEnabled();
		for (Place p : m_incoming)
			p.consume();
		for (Place p : m_outgoing)
			p.put();
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
		if (!(o instanceof Transition))
			return false;
		return equals((Transition<T>) o);
	}
	
	public boolean equals(Transition<T> t)
	{
		if (t == null)
			return false;
		return m_label.equals(t);
	}
}