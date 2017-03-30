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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.statechart.StateNode.UpStateNode;

/**
 * Representation of a UML statechart. Currently, the implementation of UML
 * statecharts imposes the following constraints and peculiarities:
 * <ul>
 * <li>A transition from a state within an orthogonal region cannot lead
 * outside of that region. It can, however, lead to a state nested
 * <em>within</em> the region.</li>
 * <li>However, transitions from the box surrounding a group of orthogonal
 * regions to somewhere else are allowed.</li>
 * <li>If no transition can fire on a given input event, the statechart
 * moves to an implicit "trash" state and stays there forever. This
 * propagates upwards, so a statechart enclosed within another one will
 * provoke the same effect on its"parent" statechart.</li>
 * </ul>
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events (i.e. the transition labels) 
 */
public class Statechart<T extends Event>
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
	 * Gets the enclosing instance of this statechart
	 * @return The instance, or {@code null} if this statechart has no parent
	 */
	public Statechart<T> getParent()
	{
		return m_parent;
	}

	/**
	 * Gets a state with given ID
	 * @param id The ID
	 * @return The state, or {@code null} if no state exists with such ID
	 */
	public State<T> getState(int id)
	{
		if (id == m_trashState.getId())
		{
			return m_trashState;
		}
		for (State<T> s : m_states.values())
		{
			if (s.m_id == id)
			{
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Returns an arbitrary state of this statechart that is not a
	 * {@link NestedState}. The method recurses inside nested states if
	 * the statechart only has nested states.
	 * This method is mostly used for the graphical
	 * rendering of a statechart.
	 * @return A state, or {@code null} if no such state exists
	 */
	public State<T> getAnyAtomicChild()
	{
		// First, try to find any direct child that is not a nested state
		for (State<T> s : m_states.values())
		{
			if (!(s instanceof NestedState))
				return s;
		}
		// If no success, recursively ask for an atomic child in all
		// nested states
		for (State<T> s : m_states.values())
		{
			// We can safely cast; if we get here, all elements are of that type
			NestedState<T> ns = (NestedState<T>) s;
			for (Statechart<T> sc : ns.m_contents)
			{
				State<T> out_state = sc.getAnyAtomicChild();
				if (out_state != null)
				{
					return out_state;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns an arbitrary state of this statechart that is not a
	 * {@link NestedState} <em>and</em> is contained within the state
	 * given as argument. This method is mostly used for the graphical
	 * rendering of a statechart.
	 * @param state The state in question
	 * @return A state, or {@code null} if no such state exists
	 */
	public State<T> getAnyAtomicChild(State<T> state)
	{
		if (!(state instanceof NestedState))
			return state;
		NestedState<T> ns = (NestedState<T>) state;
		for (Statechart<T> sc : ns.m_contents)
		{
			State<T> out_state = sc.getAnyAtomicChild();
			if (out_state != null)
			{
				return out_state;
			}
		}
		return null;
	}
	
	/**
	 * Adds a new state to this statechart
	 * @param s The state
	 * @return This statechart
	 */
	public Statechart<T> add(State<T> s)
	{
		if (m_states.isEmpty())
		{
			// By default, the first added state is the initial state
			m_initialState = s.getId();
			m_currentState = m_initialState;
		}
		m_states.put(s.getName(), s);
		if (s instanceof NestedState)
		{
			for (Statechart<T> sc : ((NestedState<T>) s).m_contents)
			{
				sc.setParent(this);
			}
		}
		return this;
	}

	/**
	 * Adds a new transition to the statechart
	 * @param source The ID of the source state
	 * @param transition The transition
	 * @return This statechart
	 */
	public Statechart<T> add(int source, Transition<T> transition)
	{
		Set<Transition<T>> set = null;
		if (!m_transitions.containsKey(source))
		{
			set = new HashSet<Transition<T>>();
		}
		else
		{
			set = m_transitions.get(source);
		}
		set.add(transition);
		m_transitions.put(source, set);
		return this;
	}

	/**
	 * Adds a new transition to the statechart
	 * @param source The name of the source state
	 * @param transition The transition
	 * @return This statechart
	 */
	public Statechart<T> add(String source_name, Transition<T> transition)
	{
		State<T> s = getState(source_name);
		if (s != null)
		{
			add(s.getId(), transition);
		}
		return this;
	}

	/**
	 * Takes a transition in this statechart with the given event
	 * @param event The event
	 * @return The transition that was taken
	 */
	public Transition<T> takeTransition(T event)
	{
		if (m_currentState == m_trashState.getId())
		{
			// Do nothing and stay in the trash sink
			return null;
		}
		Set<Transition<T>> transitions = m_transitions.get(m_currentState);
		if (transitions != null)
		{
			for (Transition<T> trans : transitions)
			{
				if (!trans.matches(event))
				{
					continue;
				}
				StateNode<T> target = trans.getTarget();
				if (applyTransition(event, target))
				{
					return trans;
				}
			}			
		}
		// If we get here, no transition with given label exists
		// from current state
		State<T> cur_state = getState(m_currentState);
		if (cur_state instanceof NestedState)
		{
			// But maybe it is a transition of the inner statechart
			NestedState<T> box = (NestedState<T>) cur_state;
			if (box.m_contents.size() == 1)
			{
				// Nested state
				Transition<T> trans = box.m_contents.get(0).takeTransition(event);
				if (trans != null)
					return trans;
			}
			else
			{
				// Orthogonal regions
				for (int i = 0; i < box.m_contents.size(); i++)
				{
					Statechart<T> sc_clone = box.m_contents.get(i).clone(null);
					Transition<T> trans = sc_clone.takeTransition(event);
					if (trans != null)
					{
						box.m_contents.get(i).takeTransition(event);
						return trans;
					}
				}
			}
		}
		// If we get here, no transition has fired; we
		// go to the "trash" sink state
		m_currentState = m_trashState.getId();
		return null;
	}

	/**
	 * Attempts to apply a transition on this statechart
	 * @param event The event to apply
	 * @param node The state node
	 * @return {@code true} if the transition could be applied, {@code false}
	 * otherwise
	 */
	public boolean applyTransition(T event, StateNode<T> node)
	{
		List<StateNode<T>> children = node.getChildren();
		if (node instanceof UpStateNode)
		{
			Statechart<T> sc = getParent();
			if (sc == null || children.isEmpty())
			{
				return false;
			}
			return sc.applyTransition(event, children.get(0));
		}
		String target_element = node.getName();
		State<T> s = getState(target_element);
		m_currentState = s.getId();
		s.reset();
		if (s instanceof NestedState)
		{
			List<Statechart<T>> contents = ((NestedState<T>) s).m_contents;
			for (int i = 0; i < children.size(); i++)
			{
				Statechart<T> sc = contents.get(i);
				StateNode<T> child = children.get(i);
				if (!sc.applyTransition(event, child))
					return false;
			}
		}
		return true;
	}

	/**
	 * Gets a state with given name
	 * @param name The name
	 * @return The state, or {@code null} if no state exists with such name
	 */
	protected State<T> getState(String name)
	{
		return m_states.get(name);
	}

	/**
	 * Gets the identifier of the current state of the statechart. If that
	 * state is itself a box containing a statechart, its state is also returned,
	 * and so on recursively. Therefore, this method returns a <em>list</em> of
	 * state IDs, with the first element being the ID of the top-most state,
	 * followed by the ID of the inner state (if any), and so on.
	 * @return The list of state IDs
	 */
	public StateNode<T> getFullState()
	{
		State<T> cur_state = getState(m_currentState);
		StateNode<T> list = new StateNode<T>(cur_state.getName());
		if (cur_state instanceof NestedState)
		{
			NestedState<T> box = (NestedState<T>) cur_state;
			for (Statechart<T> sc : box.m_contents)
			{
				list.addChild(sc.getFullState());
			}
		}
		return list;
	}

	/**
	 * Resets the statechart to its initial configuration
	 * @return This statechart
	 */
	public Statechart<T> reset()
	{
		m_currentState = m_initialState;
		for (State<T> s : m_states.values())
		{
			s.reset();
		}
		return this;
	}

	/**
	 * Creates a copy of this statechart
	 * @param parent The parent statechart, in case this statechart is enclosed 
	 * within another. Set to {@code null} otherwise
	 * @return A clone of this statechart
	 */
	public Statechart<T> clone(Statechart<T> parent) 
	{
		Statechart<T> new_sc = new Statechart<T>();
		for (Map.Entry<String,State<T>> entry : m_states.entrySet())
		{
			new_sc.m_states.put(entry.getKey(), entry.getValue().clone(new_sc));
		}
		for (Map.Entry<Integer,Set<Transition<T>>> entry : m_transitions.entrySet())
		{
			Set<Transition<T>> new_set = new HashSet<Transition<T>>();
			for (Transition<T> t : entry.getValue())
			{
				new_set.add(t.clone());
			}
			new_sc.m_transitions.put(entry.getKey(), new_set);
		}
		new_sc.m_parent = parent;
		new_sc.m_initialState = m_initialState;
		new_sc.m_currentState = m_currentState;
		return new_sc;
	}
	
	/**
	 * Counts the edges in this statechart
	 * @return The number of edges
	 */
	public int getEdgeCount()
	{
		int cnt = 0;
		cnt += m_transitions.entrySet().size();
		for (State<T> s : m_states.values())
		{
			if (s instanceof NestedState)
			{
				for (Statechart<T> sc : ((NestedState<T>) s).m_contents)
				{
					cnt += sc.getEdgeCount();
				}
			}
		}
		return cnt;
	}
	
	/**
	 * Gets the initial state of this statechart
	 * @return The initial state
	 */
	public StateNode<T> getInitialVertex()
	{
		State<T> cur_state = getState(m_initialState);
		StateNode<T> list = new StateNode<T>(cur_state.getName());
		if (cur_state instanceof NestedState)
		{
			NestedState<T> box = (NestedState<T>) cur_state;
			for (Statechart<T> sc : box.m_contents)
			{
				list.addChild(sc.getInitialVertex());
			}
		}
		return list;
	}

}
