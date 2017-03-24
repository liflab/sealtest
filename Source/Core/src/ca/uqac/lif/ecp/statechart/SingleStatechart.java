package ca.uqac.lif.ecp.statechart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.statechart.StateNode.UpStateNode;

public class SingleStatechart<T extends Event> extends Statechart<T>
{
	/**
	 * Adds a new state to this statechart
	 * @param s The state
	 * @return This statechart
	 */
	@SuppressWarnings("unchecked")
	public Statechart<T> add(State s)
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
		State s = getState(source_name);
		if (s != null)
		{
			add(s.getId(), transition);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public boolean takeTransition(T event)
	{
		if (m_currentState == m_trashState.getId())
		{
			// Do nothing and stay in the trash sink
			return false;
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
					return true;
				}
			}			
		}
		// If we get here, no transition with given label exists
		// from current state
		State cur_state = getState(m_currentState);
		if (cur_state instanceof NestedState)
		{
			// But maybe it is a transition of the inner statechart
			NestedState<T> box = (NestedState<T>) cur_state;
			for (Statechart<T> sc : box.m_contents)
			{
				boolean success = sc.takeTransition(event);
				if (success)
					return true;
			}
		}
		// If we get here, no transition has fired; we
		// go to the "trash" sink state
		m_currentState = m_trashState.getId();
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
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
		State s = getState(target_element);
		m_currentState = s.getId();
		s.reset();
		if (s instanceof NestedState)
		{
			List<Statechart<T>> contents = ((NestedState<T>) s).m_contents;
			if (contents.size() != children.size())
			{
				return false;
			}
			for (int i = 0; i < contents.size(); i++)
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
	protected State getState(String name)
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
	@SuppressWarnings("unchecked")
	public StateNode<T> getFullState()
	{
		State cur_state = getState(m_currentState);
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
	 * Gets a state with given ID
	 * @param id The ID
	 * @return The state, or {@code null} if no state exists with such ID
	 */
	protected State getState(int id)
	{
		if (id == m_trashState.getId())
		{
			return m_trashState;
		}
		for (State s : m_states.values())
		{
			if (s.m_id == id)
			{
				return s;
			}
		}
		return null;
	}

	@Override
	public Statechart<T> reset()
	{
		m_currentState = m_initialState;
		for (State s : m_states.values())
		{
			s.reset();
		}
		return this;
	}
}
