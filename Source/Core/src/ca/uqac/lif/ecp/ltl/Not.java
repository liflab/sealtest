package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class Not<T extends Event> extends UnaryOperator<T> 
{
	Not()
	{
		super("!");
	}
	
	public Not(Operator<T> operand)
	{
		super("!", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		m_operand.evaluate(event);
		Value v = m_operand.getValue();
		if (v == Value.TRUE)
		{
			m_value = Value.FALSE;
		}
		else if (v == Value.FALSE)
		{
			m_value = Value.TRUE;
		}
		else
		{
			m_value = Value.INCONCLUSIVE;
		}
	}
	
	@Override
	public int hashCode()
	{
		return m_operand.hashCode() + 1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Not<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return ((Not<T>) o).m_operand.equals(m_operand);
	}
	
	@Override
	public Not<T> copy(boolean with_tree)
	{
		Not<T> not = new Not<T>();
		super.copyInto(not, with_tree);
		return not;
	}
}
