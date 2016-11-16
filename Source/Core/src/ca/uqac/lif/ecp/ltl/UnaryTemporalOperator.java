package ca.uqac.lif.ecp.ltl;

import java.util.LinkedList;
import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

public abstract class UnaryTemporalOperator<T extends Event> extends UnaryOperator<T> 
{
	protected List<Operator<T>> m_instantiatedTrees;
	
	public UnaryTemporalOperator(String symbol)
	{
		super(symbol);
		m_instantiatedTrees = new LinkedList<Operator<T>>();
	}
	
	public UnaryTemporalOperator(String symbol, Operator<T> operand)
	{
		super(symbol, operand);
		m_instantiatedTrees = new LinkedList<Operator<T>>();
	}
	
	protected void copyInto(UnaryTemporalOperator<T> o, boolean with_tree)
	{
		super.copyInto(o, with_tree);
		o.m_operand = m_operand.copy(with_tree);
		if (with_tree)
		{
			o.m_instantiatedTrees.addAll(m_instantiatedTrees);
		}
	}
	
	@Override
	public void acceptPrefix(HologramVisitor<T> visitor)
	{
		visitor.visit(this);
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.acceptPrefix(visitor);
		}
		visitor.backtrack();
	}
	
	@Override
	public int size(boolean with_tree)
	{
		int size = 1;
		if (with_tree)
		{
			for (Operator<T> op : m_instantiatedTrees)
			{
				size += op.size(with_tree);
			}			
		}
		else
		{
			size += m_operand.size(with_tree);
		}
		return size;
	}
	
	@Override
	public List<Operator<T>> getTreeChildren()
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		list.addAll(m_instantiatedTrees);
		return list;
	}
	
	boolean chidrenEquals(UnaryTemporalOperator<T> o)
	{
		int i = 0, j = 0;
		while (i < m_instantiatedTrees.size() && j < o.m_instantiatedTrees.size())
		{
			Operator<T> op1 = m_instantiatedTrees.get(i);
			Operator<T> op2 = o.m_instantiatedTrees.get(j);
			if (op1.isDeleted())
			{
				i++;
				continue;
			}
			if (op2.isDeleted())
			{
				j++;
				continue;
			}
			if (!op1.equals(op2))
			{
				return false;
			}
			i++;
			j++;
		}
		for (int n = i; n < m_instantiatedTrees.size(); n++)
		{
			Operator<T> op = m_instantiatedTrees.get(n);
			if (!op.isDeleted())
			{
				return false;
			}
		}
		for (int n = i; n < o.m_instantiatedTrees.size(); n++)
		{
			Operator<T> op = o.m_instantiatedTrees.get(n);
			if (!op.isDeleted())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		m_operand.delete();
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.delete();
		}
	}
}
