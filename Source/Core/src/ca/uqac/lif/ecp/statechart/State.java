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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An atomic state in a statechart. Graphically, it is represented by a
 * box with a textual name.
 * @author Sylvain Hallé
 */
public class State<T extends Event>
{
	/**
	 * A name given to the state
	 */
	protected String m_name;
	
	/**
	 * A unique numerical identifier given to the state
	 */
	protected int m_id;
	
	/**
	 * A counter for giving IDs to states
	 */
	protected static int s_idCounter = 0;
	
	/**
	 * A lock for accessing the ID counter
	 */
	protected static final Lock s_counterLock = new ReentrantLock();
	
	/**
	 * Creates a new state with given name
	 * @param name The name
	 */
	public State(String name)
	{
		super();
		m_name = name;
		s_counterLock.lock();
		m_id = s_idCounter++;
		s_counterLock.unlock();
	}
	
	protected State(String name, int id)
	{
		super();
		m_name = name;
		m_id = id;
	}
	
	@Override
	public int hashCode()
	{
		return m_id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof State))
		{
			return false;
		}
		State<T> s = (State<T>) o;
		return m_id == s.m_id;
	}
	
	@Override
	public String toString()
	{
		return m_name;
	}
	
	/**
	 * Gets the state's ID
	 * @return The ID
	 */
	public int getId()
	{
		return m_id;
	}
	
	/**
	 * Gets the state's name
	 * @return The name
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Resets this state
	 */
	public void reset()
	{
		// Nothing to do
	}
	
	public State<T> clone(Statechart<T> parent)
	{
		State<T> s = new State<T>(m_name, m_id);
		return s;
	}
}
