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
		m_trashState = new State(TRASH);
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
	
	public abstract boolean takeTransition(T event);
	
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
}
