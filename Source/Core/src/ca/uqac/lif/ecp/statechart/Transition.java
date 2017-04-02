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

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.ecp.ltl.Operator.Value;

/**
 * A transition from a state to another in a statechart
 * @author Sylvain Hallé
 *
 * @param <T> The type of the event on the transition
 */
public abstract class Transition<T extends Event>
{
	/**
	 * The target of this transition
	 */
	protected Configuration<T> m_target;

	/**
	 * The action associated to this transition. Actions are optional.
	 */
	protected Action<T> m_action = null;
	
	/**
	 * A Boolean expression
	 */
	protected Operator<T> m_guard = null;
	
	/**
	 * Creates a new empty transition
	 * @param statechart The statechart this transition belongs to
	 */
	public Transition()
	{
		super();
	}
	
	/**
	 * Determines if this transition applies (i.e. fires) on the event given
	 * as input
	 * @param event The event
	 * @param statechart The statechart this transition belongs to
	 * @return {@code true} if the transition fires, {@code false} otherwise
	 */
	public final boolean matches(T event, Statechart<T> statechart)
	{
		if (m_guard == null)
		{
			return afterGuard(event, statechart);
		}
		m_guard.clear();
		m_guard.evaluate(event);
		Value v = m_guard.getValue();
		if (v == Value.TRUE)
		{
			return afterGuard(event, statechart);
		}
		return false;
	}
	
	/**
	 * Checks whether a transition fires, after the guard on that transition
	 * has been evaluated. This method is only called if the guard on that
	 * transition evaluates to {@code true}.
	 * @param event The event
	 * @param statechart The statechart this transition belongs to
	 * @return {@code true} if the transition fires, {@code false} otherwise
	 */
	protected abstract boolean afterGuard(T event, Statechart<T> statechart);
	
	@Override
	public abstract Transition<T> clone();

	/**
	 * Gets the target of this transition
	 * @return A configuration
	 */
	public Configuration<T> getTarget() 
	{
		return m_target;
	}

	/**
	 * Checks that this transition has the same parameters (guard, action,
	 * target) as another one
	 * @param t The other transition
	 * @return {@code true} if the transition has the same parameters,
	 * {@code false} otherwise
	 */
	protected boolean hasSameParameters(Transition<T> t)
	{
		if ((m_guard == null && t.m_guard != null) || (m_guard != null && !m_guard.equals(t.m_guard)))
		{
			return false;
		}
		if ((m_action == null && t.m_action != null) || (m_action != null && !m_action.equals(t.m_action)))
		{
			return false;
		}
		return m_target.equals(t.m_target);
	}
	
	/**
	 * Associates an action to this transition
	 * @param action An action. May be {@code null}; in such a case, deletes
	 * any action already associated to the transition.
	 * @return This transition
	 */
	public Transition<T> setAction(Action<T> action)
	{
		m_action = action;
		return this;
	}
	
	/**
	 * Associates a guard to this transition
	 * @param action An guard. May be {@code null}; in such a case, deletes
	 * any guard already associated to the transition.
	 * @return This transition
	 */
	public Transition<T> setGuard(Operator<T> guard)
	{
		m_guard = guard;
		return this;
	}
	
	/**
	 * Executes the action associated to this transition, if one is defined
	 * @param event The event that fired this transition
	 * @param statechart The statechart this transition belongs to
	 */
	public void executeAction(T event, Statechart<T> statechart) throws ActionException
	{
		if (m_action != null)
		{
			m_action.execute(event, this, statechart);
		}
	}
}
