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

public class Next<T extends Event> extends UnaryTemporalOperator<T> 
{	
	/**
	 * Whether we are seeing the first event
	 */
	protected int m_numEvents = 0;
	
	Next()
	{
		super("X");
	}
	
	public Next(Operator<T> operand)
	{
		super("X", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		if (m_numEvents == 0)
		{
			m_numEvents++;
			m_value = Value.INCONCLUSIVE;
			return;
		}
		if (m_numEvents == 1)
		{
			m_numEvents++;
			Operator<T> new_operand = m_operand.copy(false);
			m_instantiatedTrees.add(new_operand);
		}
		Operator<T> op = m_instantiatedTrees.get(0);
		op.evaluate(event);
		Value v = op.getValue();
		m_value = v;
	}

	@Override
	public Next<T> copy(boolean with_tree) 
	{
		Next<T> g = new Next<T>();
		super.copyInto(g, with_tree);
		if (with_tree)
		{
			g.m_numEvents = m_numEvents;
		}
		return g;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Next<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return super.chidrenEquals((Next<T>) o);
	}

}
