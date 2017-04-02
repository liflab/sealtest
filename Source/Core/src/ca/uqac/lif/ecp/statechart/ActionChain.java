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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;

/**
 * List of actions to be applied on a single transition
 * @author Sylvain Hallé
 * @param <T> The type of events in the statechart
 */
public class ActionChain<T extends Event> extends Action<T> 
{
	protected final List<Action<T>> m_actions;
	
	/**
	 * Creates an empty chain of actions
	 */
	public ActionChain()
	{
		super();
		m_actions = new ArrayList<Action<T>>();
	}
	
	/**
	 * Creates a chain of actions
	 * @param actions The actions to add to the chain
	 */
	public ActionChain(Action<T> ... actions)
	{
		super();
		m_actions = new ArrayList<Action<T>>(actions.length);
		for (Action<T> a : actions)
		{
			m_actions.add(a);
		}
	}
	
	/**
	 * Adds an action to this action chain
	 * @param action The action
	 * @return This action chain
	 */
	public ActionChain<T> add(Action<T> action)
	{
		if (action instanceof ActionChain)
		{
			// Flatten list
			for (Action<T> a : ((ActionChain<T>) action).m_actions)
			{
				add(a);
			}
		}
		else
		{
			m_actions.add(action);
		}
		return this;
	}

	@Override
	public void execute(T event, Transition<T> transition, Statechart<T> statechart) throws ActionException 
	{
		for (Action<T> action : m_actions)
		{
			action.execute(event, transition, statechart);
		}
	}

}
