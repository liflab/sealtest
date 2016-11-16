package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class Or<T extends Event> extends NaryOperator<T> 
{
	public Or()
	{
		super("|");
	}
	
	public Or(Object ... ops)
	{
		super("|", ops);
	}

	@Override
	public void evaluate(T event) 
	{
		boolean true_seen = false;
		boolean inconclusive_seen = false;
		for (Operator<T> op : m_operands)
		{
			op.evaluate(event);
			Value v = op.getValue();
			if (v == Value.TRUE)
			{
				true_seen = true;
			}
			if (v == Value.INCONCLUSIVE)
			{
				inconclusive_seen = true;
			}
		}
		if (true_seen)
		{
			m_value = Value.TRUE;
			return;
		}
		if (inconclusive_seen)
		{
			m_value = Value.INCONCLUSIVE;
			return;
		}
		m_value = Value.FALSE;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode(1000);
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
		return childrenEquals((Or<T>) o);
	}
	
	@Override
	public Or<T> copy(boolean with_tree)
	{
		Or<T> or = new Or<T>();
		super.copyInto(or, with_tree);
		return or;
	}
}
