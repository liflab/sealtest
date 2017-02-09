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

import java.util.*;

import ca.uqac.lif.ecp.Event;

/**
 * Generates a trace of events based on the definition
 * of a Petri net. For more information about Petri nets,
 * see <a href="http://en.wikipedia.org/wiki/Petri_net">Wikipedia's entry</a>.
 * <p>
 * See {@link parseFromString} for a precise definition of the
 * input format to specify the Petri net.
 * @author Sylvain Hallé
 *
 */
public class PetriNet<T extends Event>
{
	/**
	 * The list of places in the Petri net
	 */
	protected List<Place<T>> m_places;

	/**
	 * The list of transitions in the Petri net
	 */
	protected List<Transition<T>> m_transitions;
	
	/**
	 * The list of places that contain a token in the initial marking
	 */
	protected Map<String,Integer> m_initialMarking;
	
	/**
	 * Creates an empty Petri Net
	 */
	public PetriNet()
	{
		super();
		m_places = new ArrayList<Place<T>>();
		m_transitions = new ArrayList<Transition<T>>();
		m_initialMarking = new HashMap<String,Integer>();
	}
	
	/**
	 * Resets the Petri net to its initial markings
	 */
	public void reset()
	{
		for (Place<T> p : m_places)
		{
			p.setMarking(0);
		}
		for (Map.Entry<String,Integer> entries : m_initialMarking.entrySet())
		{
			Place<T> p = getPlace(entries.getKey());
			if (p != null)
			{
				// ...which should be the case
				p.setMarking(entries.getValue());
			}
		}
	}
	
	/**
	 * Gets the place with given label
	 * @param label The label
	 * @return The place, or {@code null} if the place was not found
	 */
	protected Place<T> getPlace(String label)
	{
		for (Place<T> p : m_places)
		{
			if (p.m_label.compareTo(label) == 0)
				return p;
		}
		return null;
	}
	
	/**
	 * Attempts to fire a transition in the current state of the
	 * Petri net.
	 * @param t The transition to fire
	 * @return {@code true} if the transition can fire, {@code false}
	 * otherwise. If the transition fires, the Petri net's state is
	 * updated accordingly. 
	 */
	public boolean fire(Transition<T> t)
	{
		// TODO
		return false;
	}
	
	/**
	 * Adds tokens to the initial marking of this Petri net.
	 * @param label The label of the place where tokens should be added
	 * @param value The number of tokens to put there
	 */
	public void setInitialMarking(String label, int value)
	{
		m_initialMarking.put(label, value);
	}
	
	/**
	 * Given a place p: finds and returns the element in the list of places
	 * if it exists, or adds it to the list and returns p if it doesn't.
	 * @param p The place to look for
	 * @return
	 */
	Place<T> getAddPlace(Place<T> p)
	{
		int i = m_places.lastIndexOf(p);
		if (i == -1)
			m_places.add(p);
		else
			p = m_places.get(i);
		return p;
	}
	
	/**
	 * Given a transition t: finds and returns the element in the list of transitions
	 * if it exists, or adds it to the list and returns t if it doesn't.
	 * @param t The transition to look for
	 * @return
	 */
	Transition<T> getAddTransition(Transition<T> t)
	{
		int i = m_transitions.lastIndexOf(t);
		if (i == -1)
			m_transitions.add(t);
		else
			t = m_transitions.get(i);
		return t;
	}
}
