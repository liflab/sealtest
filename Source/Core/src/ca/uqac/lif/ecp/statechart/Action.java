package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;

/**
 * Action executed when a transition is fired in a statechart.
 * Typically, an action performs some external side effect or modifies
 * a statechart's internal variables.
 * 
 * @param <T> The type of the events in the statechart
 * 
 * @author Sylvain Hallé
 */
public abstract class Action<T extends Event>
{
	/**
	 * Executes the action
	 * @param event The event that fires the transition to which 
	 *   this action is associated
	 * @param transition The transition to which this action is associated
	 * @param statechart The statechart this transition belongs to
	 */
	public abstract void execute(T event, Transition<T> transition, Statechart<T> statechart) throws ActionException;
}
