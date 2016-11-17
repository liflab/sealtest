/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp.ltl;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

/**
 * Basic functionalities associated to a <i>n</i>-ary logical operator
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
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
	
	@Override
	public void addOperand(Operator<T> op)
	{
		m_operands.add(op);
	}

	/**
	 * Computes the hash code of this n-ary operator, starting from
	 * an initial value 
	 * @param start_value The initial value
	 * @return The hash code
	 */
	protected int hashCode(int start_value)
	{
		for (Operator<T> op : m_operands)
		{
			start_value += op.hashCode();
		}
		return start_value;
	}

	/**
	 * Checks whether the children of this operator are equal to that of
	 * another n-ary operator. This check takes into account the fact that 
	 * children that are marked as deleted in either operator should be 
	 * skipped.
	 * @param o The other operator
	 * @return true if their children are equal, false otherwise
	 */
	protected boolean childrenEquals(NaryOperator<T> o)
	{
		int i = 0, j = 0;
		while (i < m_operands.size() && j < o.m_operands.size())
		{
			Operator<T> op1 = m_operands.get(i);
			Operator<T> op2 = o.m_operands.get(j);
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
		for (int n = i; n < m_operands.size(); n++)
		{
			Operator<T> op = m_operands.get(n);
			if (!op.isDeleted())
			{
				return false;
			}
		}
		for (int n = i; n < o.m_operands.size(); n++)
		{
			Operator<T> op = o.m_operands.get(n);
			if (!op.isDeleted())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Copies the internal content of this operator into a new instance
	 * @param o The new instance
	 * @param with_tree Set to <code>true</code> to also copy data related
	 *   to the operator's evaluation tree
	 */
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
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree)
	{
		visitor.visit(this);
		for (Operator<T> op : m_operands)
		{
			op.acceptPrefix(visitor, in_tree);
		}
		visitor.backtrack();
	}
	
	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree)
	{
		for (Operator<T> op : m_operands)
		{
			op.acceptPostfix(visitor, in_tree);
		}
		visitor.visit(this);
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
			if (!op.isDeleted())
			{
				size += op.size(with_tree);
			}
		}
		return size;
	}
	
	/**
	 * Gets the number of children in the expression
	 * @return The number of children
	 */
	public int childrenCount()
	{
		return m_operands.size();
	}

	@Override
	public List<Operator<T>> getTreeChildren()
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		list.addAll(m_operands);
		return list;
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		for (Operator<T> op : m_operands)
		{
			op.delete();
		}
	}
}
