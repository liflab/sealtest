package ca.uqac.lif.ecp.ltl;

import java.util.LinkedList;
import java.util.List;

import ca.uqac.lif.ecp.Event;

public abstract class UnaryTemporalOperator<T extends Event> extends UnaryOperator<T> 
{
	protected List<Operator<T>> m_instantiatedTrees;
	
	public UnaryTemporalOperator(String symbol)
	{
		super(symbol);
	}
	
	public UnaryTemporalOperator(String symbol, Operator<T> operand)
	{
		super(symbol, operand);
		m_instantiatedTrees = new LinkedList<Operator<T>>();
	}
}
