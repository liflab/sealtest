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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.Event;

/**
 * Representation of an UML statechart
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events (i.e. the transition labels) 
 */
public abstract class Statechart<T extends Event>
{
	/**
	 * Symbol indicating that the target of a transition refers to a state
	 * in the parent of the current statechart
	 */
	public static final String UP = "..";
	
	/**
	 * Label given to the trash sink in the statechart
	 */
	public static final String TRASH = "TRASH";
	
	/**
	 * The set of states contained in this statechart
	 */
	protected Map<String,State<T>> m_states;
	
	/**
	 * The map of transitions in this statechart. The map's keys are the
	 * IDs of the source states, and the values are the outgoing transitions
	 * from this state.
	 */
	protected Map<Integer,Set<Transition<T>>> m_transitions;
	
	/**
	 * The ID of the current state in the execution of the statechart
	 */
	protected int m_currentState;
	
	/**
	 * The ID of the initial state. 0 by default.
	 */
	protected int m_initialState = 0;
	
	/**
	 * A special sink state to make sure the transition relation is total
	 */
	protected final State<T> m_trashState;
	
	/**
	 * The parent statechart, if any
	 */
	protected Statechart<T> m_parent = null;
	
	/**
	 * Creates a new empty statechart
	 */
	public Statechart()
	{
		super();
		m_states = new HashMap<String,State<T>>();
		m_transitions = new HashMap<Integer,Set<Transition<T>>>();
		m_trashState = new State<T>(TRASH);
	}
	
	/**
	 * Sets the enclosing instance of this statechart
	 * @param parent The instance
	 * @return This statechart
	 */
	public Statechart<T> setParent(Statechart<T> parent)
	{
		m_parent = parent;
		return this;
	}

	/**
	 * Resets the statechart to its initial state
	 * @return This statechart
	 */
	public abstract Statechart<T> reset();
	
	/**
	 * Takes a transition in the statechart
	 * @param event The event
	 * @return The transition that was taken, or {@code null} if no
	 * valid transtition exists 
	 */
	public abstract Transition<T> takeTransition(T event);
	
	/**
	 * Gets the identifier of the current state of the statechart. If that
	 * state is itself a box containing a statechart, its state is also returned,
	 * and so on recursively. Therefore, this method returns a <em>list</em> of
	 * state IDs, with the first element being the ID of the top-most state,
	 * followed by the ID of the inner state (if any), and so on.
	 * @return The list of state IDs
	 */
	public abstract StateNode<T> getFullState();

	/**
	 * Gets the enclosing instance of this statechart
	 * @return The instance, or {@code null} if this statechart has no parent
	 */
	public Statechart<T> getParent()
	{
		return m_parent;
	}
	
	public abstract boolean applyTransition(T event, StateNode<T> node);
	
	/**
	 * Gets a copy of this statechart
	 * @param parent The parent of this statechart, if any
	 */
	public abstract Statechart<T> clone(Statechart<T> parent);
	
	/**
	 * Gets the number of edges in this statechart
	 * @return The number of edges
	 */
	public abstract int getEdgeCount();

	/**
	 * Gets the initial state of this statechart
	 * @return The initial state
	 */
	public abstract StateNode<T> getInitialVertex();
}
