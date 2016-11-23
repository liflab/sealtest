/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

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
package ca.uqac.lif.ecp.online;

import ca.uqac.lif.ecp.Event;

/**
 * Calls a hook based on a test suite
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public abstract class TestDriver<T extends Event,C> implements Runnable
{
	/**
	 * The hook that the driver will call during the tests
	 */
	protected TestHook<T,C> m_hook;
	
	/**
	 * The number of events the driver has executed so far
	 */
	protected int m_executedEvents = 0;
	
	/**
	 * The number of sequences in the test suite that the driver has
	 * completed so far
	 */
	protected int m_completedTraces = 0;
	
	/**
	 * Creates a new empty test driver
	 */
	public TestDriver()
	{
		super();
	}
		
	/**
	 * Resets the test driver
	 */
	public void reset()
	{
		m_completedTraces = 0;
		m_executedEvents = 0;
	}
		
	/**
	 * Sets the hook that the driver will call during the tests
	 * @param hook The hook
	 */
	public void setHook(TestHook<T,C> hook)
	{
		m_hook = hook;
	}
	
	/**
	 * Gets the number of traces that have been fully executed by 
	 * the driver so far
	 * @return The number of traces
	 */
	public int getNumCompletedTraces()
	{
		return m_completedTraces;
	}

	/**
	 * Gets the total number of events that have been executed by 
	 * the driver so far
	 * @return The number of events
	 */
	public int getNumExecutedEvents()
	{
		return m_executedEvents;
	}
		
	/**
	 * Tells the driver to stop testing
	 */
	public abstract void stop();
	
	/**
	 * Tells the driver that the hook produced an event
	 * @param event The event
	 */
	public abstract C execute(T event);
}
