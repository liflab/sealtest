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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.Trace;

/**
 * Test driver that executes events sequentially and ignores any
 * event sent back by the test hook. In other words, a unidirectional
 * driver is not interactive: its behaviour does not depend on what
 * the test hook does. 
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class UnidirectionalTestDriver<T extends Event,C> extends TestDriver<T,C>
{
	/**
	 * The test suite that the driver will replay. We allow this
	 * test suite to be an <em>ordered</em> set of traces, so that
	 * the user of the test driver can prioritize what sequences
	 * should be tried first.
	 */
	protected List<Trace<T>> m_testSuite;
	
	/**
	 * An iterator over the traces of the test suite
	 */
	protected Iterator<Trace<T>> m_traceIterator;
	
	/**
	 * An iterator over the events of a trace
	 */
	protected Iterator<T> m_eventIterator;

	/**
	 * Whether the driver is done playing back all traces
	 */
	protected boolean m_done = false;
	
	/**
	 * Semaphore indicating whether the driver should be stopped
	 */
	boolean m_run = true;

	/**
	 * Creates a new driver
	 */
	public UnidirectionalTestDriver()
	{
		super();
		m_eventIterator = null;
		m_traceIterator = null;
		m_testSuite = null;
	}
	
	/**
	 * Creates a new driver, and gives it a set of traces to play back
	 * to its hook
	 * @param suite The set of sequences. If the collection passed to this
	 *   constructor is ordered (such as a list), the driver will play the
	 *   traces in the order they appear in this collection.
	 */
	public UnidirectionalTestDriver(Collection<Trace<T>> suite)
	{
		this();
		setTestSuite(suite);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_eventIterator = null;
		m_traceIterator = null;
	}
	
	/**
	 * Sets the test suite that the driver will replay
	 * @param suite The test suite
	 */
	public void setTestSuite(Collection<Trace<T>> suite)
	{
		m_testSuite = new ArrayList<Trace<T>>(suite.size());
		m_testSuite.addAll(suite);
	}

	@Override
	public void run()
	{
		m_run = true;
		while (m_run)
		{
			if (m_traceIterator == null)
			{
				m_traceIterator = m_testSuite.iterator();
			}
			if (m_eventIterator == null)
			{
				if (!m_traceIterator.hasNext())
				{
					m_done = true;
					break;
				}
				Trace<T> trace = m_traceIterator.next();
				m_eventIterator = trace.iterator();
			}
			T event = null;
			if (m_eventIterator.hasNext())
			{
				event = m_eventIterator.next();
			}
			else
			{
				boolean found = false;
				while (m_traceIterator.hasNext())
				{
					Trace<T> trace = m_traceIterator.next();
					m_hook.reset();
					m_completedTraces++;
					m_eventIterator = trace.iterator();
					if (m_eventIterator.hasNext())
					{
						found = true;
						event = m_eventIterator.next();
						break;
					}
				}
				if (!found)
				{
					m_done = true;
					break;
				}
			}
			if (m_hook == null)
			{
				// Something wrong occurred
				m_done = true;
				break;
			}
			m_hook.execute(event);
			m_executedEvents++;
		}
	}
	
	@Override
	public void stop()
	{
		m_run = false;
	}

	@Override
	public final C execute(T event) 
	{
		// A unidirectional driver ignores any event sent back by the hook
		return null;
	}
}
