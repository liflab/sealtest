package ca.uqac.lif.ecp.statechart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.Event;

/**
 * Representation of an UML statechart
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
	 * The set of states contained in this statechart
	 */
	protected Map<String,State> m_states;
	
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
	protected final State m_trashState;
	
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
		m_states = new HashMap<String,State>();
		m_transitions = new HashMap<Integer,Set<Transition<T>>>();
		m_trashState = new State("TRASH");
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
	 * Adds a new state to this statechart
	 * @param s The state
	 * @return This statechart
	 */
	public Statechart<T> add(State s)
	{
		if (m_states.isEmpty())
		{
			// By default, the first added state is the initial state
			m_initialState = s.getId();
			m_currentState = m_initialState;
		}
		m_states.put(s.getName(), s);
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
	
	/**
	 * Resets the statechart to its initial state
	 * @return This statechart
	 */
	public Statechart<T> reset()
	{
		m_currentState = m_initialState;
		for (State s : m_states.values())
		{
			s.reset();
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
				List<String> target = trans.getTarget();
				Statechart<T> sc = this;
				for (int i = 0; i < target.size(); i++)
				{
					String target_element = target.get(i);
					if (target_element.compareTo(UP) == 0)
					{
						sc = getParent();
					}
					else
					{
						State s = getState(target_element);
						sc.m_currentState = s.getId();
						s.reset();
						if (s instanceof BoxState && i < target.size() - 1)
						{
							sc = ((BoxState<T>) s).m_contents;
						}					
					}
				}
				return true;
			}			
		}
		// If we get here, no transition with given label exists
		// from current state
		State cur_state = getState(m_currentState);
		if (cur_state instanceof BoxState)
		{
			// But maybe it is a transition of the inner statechart
			BoxState<T> box = (BoxState<T>) cur_state;
			Statechart<T> sc = box.m_contents;
			boolean success = sc.takeTransition(event);
			if (success)
				return true;
		}
		// If we get here, no transition has fired; we
		// go to the "trash" sink state
		m_currentState = m_trashState.getId();
		return false;
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
	public List<String> getCurrentState()
	{
		List<String> list = new ArrayList<String>();
		Statechart<T> sc = this;
		while (sc != null)
		{
			list.add(sc.getState(sc.m_currentState).getName());
			State cur_state = sc.getState(m_currentState);
			if (cur_state instanceof BoxState)
			{
				// But maybe it is a transition of the inner statechart
				BoxState<T> box = (BoxState<T>) cur_state;
				sc = box.m_contents;
			}
			else
			{
				break;
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
	 * Gets the enclosing instance of this statechart
	 * @return The instance, or {@code null} if this statechart has no parent
	 */
	public Statechart<T> getParent()
	{
		return m_parent;
	}
}
