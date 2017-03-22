package ca.uqac.lif.ecp.statechart;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An atomic state in a statechart. Graphically, it is represented by a
 * box with a textual name.
 * @author Sylvain Hallé
 */
public class State 
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
	
	@Override
	public int hashCode()
	{
		return m_id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof State))
		{
			return false;
		}
		State s = (State) o;
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
}
