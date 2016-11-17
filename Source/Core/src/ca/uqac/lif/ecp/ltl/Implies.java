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

import ca.uqac.lif.ecp.Event;

public class Implies<T extends Event> extends NaryOperator<T> 
{
	public Implies()
	{
		super("->");
	}
	
	public Implies(Object ... ops)
	{
		super("->", ops);
	}

	@Override
	public void evaluate(T event) 
	{
		Operator<T> op1 = m_operands.get(0);
		Operator<T> op2 = m_operands.get(1);
		op1.evaluate(event);
		op2.evaluate(event);
		if (op1.getValue() == Value.FALSE || op2.getValue() == Value.TRUE)
		{
			m_value = Value.TRUE;
			return;
		}
		if (op1.getValue() == Value.TRUE && op2.getValue() == Value.FALSE)
		{
			m_value = Value.FALSE;
			return;
		}
		m_value = Value.INCONCLUSIVE;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode(2000);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Implies<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return childrenEquals((Implies<T>) o);
	}
	
	@Override
	public Implies<T> copy(boolean with_tree)
	{
		Implies<T> or = new Implies<T>();
		super.copyInto(or, with_tree);
		return or;
	}
}
