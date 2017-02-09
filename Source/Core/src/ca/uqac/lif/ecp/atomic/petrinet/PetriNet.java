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
	protected List<Place> m_places;

	
	/**
	 * The list of transitions in the Petri net
	 */
	protected List<Transition<T>> m_transitions;
	
	/**
	 * Creates an empty Petri Net
	 */
	public PetriNet()
	{
		super();
		m_places = new ArrayList<Place>();
		m_transitions = new ArrayList<Transition<T>>();
	}
	
	
	/**
	 * Given a place p: finds and returns the element in the list of places
	 * if it exists, or adds it to the list and returns p if it doesn't.
	 * @param p The place to look for
	 * @return
	 */
	Place getAddPlace(Place p)
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
