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

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

/**
 * Basic functionalities associated to a unary logical operator
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public abstract class UnaryOperator<T extends Event> extends Operator<T> 
{
	/**
	 * The operand of this unary operator
	 */
	protected Operator<T> m_operand;
	
	/**
	 * The symbol to display for this operator
	 */
	protected final String m_symbol;
	
	/**
	 * Creates a new unary operator
	 * @param symbol The symbol to display for this operator
	 */
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
	
	@Override
	public void addOperand(Operator<T> op)
	{
		m_operand = op;
	}
	
	/**
	 * Copies the internal content of this operator into a new instance
	 * @param o The new instance
	 * @param with_tree Set to <code>true</code> to also copy data related
	 *   to the operator's evaluation tree
	 */
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
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree)
	{
		visitor.visit(this);
		m_operand.acceptPrefix(visitor, in_tree);
		visitor.backtrack();
	}
	
	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree)
	{
		m_operand.acceptPostfix(visitor, in_tree);
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
	
	@Override
	public void clear()
	{
		super.clear();
		m_operand.clear();
	}
}
