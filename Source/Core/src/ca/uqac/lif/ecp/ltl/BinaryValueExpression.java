/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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

import ca.uqac.lif.ecp.Event;

public abstract class BinaryValueExpression<T extends Event,U,V> extends ValueExpression<T,V> 
{
	/**
	 * The value of the left-hand side expression
	 */
	protected ConcreteValue<T,U> m_leftValue;
	
	/**
	 * The value of the right-hand side expression
	 */
	protected ConcreteValue<T,U> m_rightValue;
	
	public BinaryValueExpression(ConcreteValue<T,U> left, ConcreteValue<T,U> right)
	{
		super();
		m_leftValue = left;
		m_rightValue = right;
	}
	
	@Override
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}

	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}
	
	@Override
	public String toString()
	{
		return m_leftValue + " " + getRootSymbol() + " " + m_rightValue;
	}

}
