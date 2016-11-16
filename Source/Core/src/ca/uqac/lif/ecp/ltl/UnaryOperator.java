package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public abstract class UnaryOperator<T extends Event> extends Operator<T> 
{
	protected Operator<T> m_operand;
	
	protected final String m_symbol;
	
	public UnaryOperator(String symbol)
	{
		super();
		m_symbol = symbol;
	}
	
	public UnaryOperator(String symbol, Operator<T> operand)
	{
		super();
		m_symbol = symbol;
		m_operand = operand;
	}
	
	protected void copyInto(UnaryOperator<T> o, boolean with_tree)
	{
		super.copyInto(o, with_tree);
		o.m_operand = m_operand.copy(with_tree);
	}
	
	@Override
	public String toString()
	{
		return m_symbol + " (" + m_operand + ")";
	}
}
