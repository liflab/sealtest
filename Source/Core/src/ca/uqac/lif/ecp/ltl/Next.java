package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class Next<T extends Event> extends UnaryTemporalOperator<T> 
{	
	/**
	 * Whether we are seeing the first event
	 */
	protected int m_numEvents = 0;
	
	Next()
	{
		super("X");
	}
	
	public Next(Operator<T> operand)
	{
		super("X", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		if (m_numEvents == 0)
		{
			m_numEvents++;
			m_value = Value.INCONCLUSIVE;
			return;
		}
		if (m_numEvents == 1)
		{
			m_numEvents++;
			Operator<T> new_operand = m_operand.copy(false);
			m_instantiatedTrees.add(new_operand);
		}
		Operator<T> op = m_instantiatedTrees.get(0);
		op.evaluate(event);
		Value v = op.getValue();
		m_value = v;
	}

	@Override
	public Next<T> copy(boolean with_tree) 
	{
		Next<T> g = new Next<T>();
		super.copyInto(g, with_tree);
		if (with_tree)
		{
			g.m_numEvents = m_numEvents;
		}
		return g;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Next<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return super.chidrenEquals((Next<T>) o);
	}

}
