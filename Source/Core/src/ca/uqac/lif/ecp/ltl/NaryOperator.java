package ca.uqac.lif.ecp.ltl;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

public abstract class NaryOperator<T extends Event> extends Operator<T>
{
	protected List<Operator<T>> m_operands;
	
	protected final String m_symbol;
	
	public NaryOperator(String symbol)
	{
		super();
		m_symbol = symbol;
		m_operands = new ArrayList<Operator<T>>();
	}
	
	@SuppressWarnings("unchecked")
	public NaryOperator(String symbol, Object ... ops)
	{
		super();
		m_symbol = symbol;
		m_operands = new ArrayList<Operator<T>>();
		for (Object o : ops)
		{
			if (o instanceof Operator<?>)
			{
				m_operands.add((Operator<T>) o);
			}
		}
	}
	
	protected int hashCode(int start_value)
	{
		for (Operator<T> op : m_operands)
		{
			start_value += op.hashCode();
		}
		return start_value;
	}
	
	protected boolean childrenEquals(NaryOperator<T> o)
	{
		if (o.m_operands.size() != m_operands.size())
		{
			return false;
		}
		for (int i = 0; i < m_operands.size(); i++)
		{
			Operator<T> op1 = m_operands.get(i);
			Operator<T> op2 = o.m_operands.get(i);
			if (!op1.equals(op2))
			{
				return false;
			}
		}
		return true;
	}
	
	protected void copyInto(NaryOperator<T> o, boolean with_tree)
	{
		super.copyInto(o, with_tree);
		for (Operator<T> op : m_operands)
		{
			o.m_operands.add(op.copy(with_tree));
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < m_operands.size(); i++)
		{
			if (i > 0)
			{
				out.append(" ").append(m_symbol).append(" ");
			}
			out.append("(").append(m_operands.get(i)).append(")");
		}
		return out.toString();
	}
	
	@Override
	public void acceptPrefix(HologramVisitor<T> visitor)
	{
		visitor.visit(this);
		for (Operator<T> op : m_operands)
		{
			op.acceptPrefix(visitor);
		}
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
		int size = 1;
		for (Operator<T> op : m_operands)
		{
			size += op.size(with_tree);
		}
		return size;
	}
	
	@Override
	public List<Operator<T>> getTreeChildren()
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		list.addAll(m_operands);
		return list;
	}
}