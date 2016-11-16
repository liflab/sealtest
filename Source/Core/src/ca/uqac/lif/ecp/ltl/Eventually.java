package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class Eventually<T extends Event> extends UnaryTemporalOperator<T> 
{	
	public Eventually()
	{
		super("F");
	}
	
	public Eventually(Operator<T> operand)
	{
		super("F", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		Operator<T> new_operand = m_operand.copy(false);
		m_instantiatedTrees.add(new_operand);
		boolean true_seen = false;
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.evaluate(event);
			Value v = op.getValue();
			if (v == Value.TRUE)
			{
				true_seen = true;
			}
		}
		if (true_seen)
		{
			m_value = Value.TRUE;
			return;
		}
		m_value = Value.INCONCLUSIVE;
	}

	@Override
	public Eventually<T> copy(boolean with_tree) 
	{
		Eventually<T> g = new Eventually<T>();
		super.copyInto(g, with_tree);
		return g;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Eventually<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return super.chidrenEquals((Eventually<T>) o);
	}
}
