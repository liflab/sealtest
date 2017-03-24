package ca.uqac.lif.ecp.statechart;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

	@Override
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
		State<T> cur_state = getState(m_currentState);
		if (cur_state instanceof NestedState)
		{
			// But maybe it is a transition of the inner statechart
			NestedState<T> box = (NestedState<T>) cur_state;
			if (box.m_contents.size() == 1)
			{
				// Nested state
				boolean success = box.m_contents.get(0).takeTransition(event);
				if (success)
					return true;
			}
			else
			{
				// Orthogonal regions
				for (int i = 0; i < box.m_contents.size(); i++)
				{
					Statechart<T> sc_clone = box.m_contents.get(i).clone(null);
					boolean success = sc_clone.takeTransition(event);
					if (success)
					{
						box.m_contents.get(i).takeTransition(event);
						return true;
					}
				}
			}
		}
		// If we get here, no transition has fired; we
		// go to the "trash" sink state
		m_currentState = m_trashState.getId();
		return false;
	}

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
	 * Gets a state with given ID
	 * @param id The ID
	 * @return The state, or {@code null} if no state exists with such ID
	 */
	protected State<T> getState(int id)
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

	@Override
	public Statechart<T> reset()
	{
		m_currentState = m_initialState;
		for (State<T> s : m_states.values())
		{
			s.reset();
		}
		return this;
	}

	@Override
	public Statechart<T> clone(Statechart<T> parent) 
	{
		SingleStatechart<T> new_sc = new SingleStatechart<T>();
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
}
