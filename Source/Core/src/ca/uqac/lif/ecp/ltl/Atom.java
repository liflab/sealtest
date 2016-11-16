package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

/**
 * Ground term that asserts that the current event is equal
 * to the event associated to this term
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class Atom<T extends Event> extends Operator<T>
{
	protected final T m_event;
	
	public Atom(T e)
	{
		super();
		m_event = e;
	}

	@Override
	public void evaluate(T event) 
	{
		if (event.equals(m_event))
		{
			m_value = Value.TRUE;
			return;
		}
		m_value = Value.FALSE;
	}
	
	@Override
	public String toString()
	{
		return m_event.toString();
	}
	
	@Override
	public int hashCode()
	{
		return m_event.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Atom<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		@SuppressWarnings("unchecked")
		Atom<T> a = (Atom<T>) o;
		return a.m_event.equals(m_event);
	}
	
	@Override
	public Atom<T> copy(boolean with_tree)
	{
		return new Atom<T>(m_event);
	}

}
