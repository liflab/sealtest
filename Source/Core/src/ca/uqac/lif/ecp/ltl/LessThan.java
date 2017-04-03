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
import ca.uqac.lif.ecp.util.TypeHelper;

/**
 * Expression comparing the value of two quantities. It returns true
 * when the first is less than the second
 * @author Sylvain Hallé
 *
 * @param <T> The type of events
 */
public class LessThan<T extends Event> extends BinaryValueExpression<T,Number,Operator.Value> 
{
	public LessThan(ConcreteValue<T,Number> left, ConcreteValue<T,Number> right) 
	{
		super(left, right);
	}

	@Override
	public LessThan<T> copy(boolean with_tree) 
	{
		return new LessThan<T>(m_leftValue, m_rightValue);
	}

	@Override
	public void evaluate(T event) 
	{
		if (event == null)
		{
			return;
		}
		if (m_eventSeen == null)
		{
			m_eventSeen = new EventLeaf<T>(event);
			Number left = m_leftValue.getValue(event);
			Number right = m_rightValue.getValue(event);
			if (TypeHelper.castToDouble(left) < TypeHelper.castToDouble(right))
			{
				m_value = Value.TRUE;
				return;
			}
			m_value = Value.FALSE;
		}
	}

	@Override
	public String getRootSymbol() 
	{
		return "<";
	}

	@Override
	public void clean() 
	{
		m_eventSeen = null;
		m_value = Value.INCONCLUSIVE;
	}

}
