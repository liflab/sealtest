package ca.uqac.lif.ecp.ltl;

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

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
	
	@Override
	public void acceptPrefix(HologramVisitor<T> visitor)
	{
		visitor.visit(this);
		m_operand.acceptPrefix(visitor);
		visitor.backtrack();
	}
	
	@Override
	public String getRootSymbol()
	{
		return m_symbol;
	}
	
	@Override
	public int size(boolean with_tree)
	{
		return 1 + m_operand.size(with_tree);
	}
	
	@Override
	public List<Operator<T>> getTreeChildren()
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		list.add(m_operand);
		return list;
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		m_operand.delete();
	}
}
