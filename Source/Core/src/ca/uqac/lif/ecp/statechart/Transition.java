package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;

/**
 * A transition from a state to another in a statechart
 * @author Sylvain Hallé
 *
 * @param <T> The type of the event on the transition
 */
public abstract class Transition<T extends Event>
{
	public abstract boolean matches(T event);
	
	public abstract StateNode<T> getTarget();
	
	@Override
	public abstract Transition<T> clone();
}
