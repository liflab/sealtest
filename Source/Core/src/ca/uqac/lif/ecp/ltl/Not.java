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
		if (m_operand.isDeleted())
		{
			return 1;
		}
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
		Not<T> a = (Not<T>) o;
		if (m_value != a.m_value)
		{
			return false;
		}
		return (m_operand.isDeleted() && a.m_operand.isDeleted()) || ((Not<T>) o).m_operand.equals(m_operand);
	}
	
	@Override
	public Not<T> copy(boolean with_tree)
	{
		Not<T> not = new Not<T>();
		super.copyInto(not, with_tree);
		return not;
	}
}
