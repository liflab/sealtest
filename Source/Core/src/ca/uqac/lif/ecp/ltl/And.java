package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class And<T extends Event> extends NaryOperator<T> 
{
	public And()
	{
		super("&");
	}
	
	public And(Object ... ops)
	{
		super("&", ops);
	}

	@Override
	public void evaluate(T event) 
	{
		boolean false_seen = false;
		boolean inconclusive_seen = false;
		for (Operator<T> op : m_operands)
		{
			op.evaluate(event);
			Value v = op.getValue();
			if (v == Value.FALSE)
			{
				false_seen = true;
			}
			if (v == Value.INCONCLUSIVE)
			{
				inconclusive_seen = true;
			}
		}
		if (false_seen)
		{
			m_value = Value.FALSE;
			return;
		}
		if (inconclusive_seen)
		{
			m_value = Value.INCONCLUSIVE;
			return;
		}
		m_value = Value.TRUE;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Or<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return childrenEquals((And<T>) o);
	}

	@Override
	public And<T> copy(boolean with_tree)
	{
		And<T> and = new And<T>();
		super.copyInto(and, with_tree);
		return and;
	}
}
