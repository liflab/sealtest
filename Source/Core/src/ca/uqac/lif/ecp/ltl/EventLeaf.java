package ca.uqac.lif.ecp.ltl;

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

/**
 * Ground term that asserts that the current event is equal
 * to the event associated to this term
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class EventLeaf<T extends Event> extends Operator<T>
{
	protected final T m_event;
			
	public EventLeaf(T e)
	{
		super();
		m_event = e;
	}

	@Override
	public void evaluate(T event) 
	{
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
		if (o == null || !(o instanceof EventLeaf<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		@SuppressWarnings("unchecked")
		EventLeaf<T> a = (EventLeaf<T>) o;
		return a.m_event.equals(m_event);
	}
	
	@Override
	public EventLeaf<T> copy(boolean with_tree)
	{
		EventLeaf<T> a = new EventLeaf<T>(m_event);
		return a;
	}

	@Override
	public void acceptPrefix(HologramVisitor<T> visitor)
	{
		visitor.visit(this);
		visitor.backtrack();
	}
	
	@Override
	public String getRootSymbol()
	{
		return m_event.toString();
	}

	@Override
	public int size(boolean with_tree) 
	{
		return 1;
	}

	@Override
	public List<Operator<T>> getTreeChildren() 
	{
		return new MathList<Operator<T>>();
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
	}
}
