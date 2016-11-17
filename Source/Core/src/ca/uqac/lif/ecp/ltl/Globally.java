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

public class Globally<T extends Event> extends UnaryTemporalOperator<T> 
{	
	public Globally()
	{
		super("G");
	}
	
	public Globally(Operator<T> operand)
	{
		super("G", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		Operator<T> new_operand = m_operand.copy(false);
		m_instantiatedTrees.add(new_operand);
		boolean false_seen = false;
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.evaluate(event);
			Value v = op.getValue();
			if (v == Value.FALSE)
			{
				false_seen = true;
			}
		}
		if (false_seen)
		{
			m_value = Value.FALSE;
			return;
		}
		m_value = Value.INCONCLUSIVE;
	}

	@Override
	public Globally<T> copy(boolean with_tree) 
	{
		Globally<T> g = new Globally<T>();
		super.copyInto(g, with_tree);
		return g;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Globally<?>))
		{
			return false;
		}
		if (o == this)
		{
			return true;
		}
		return super.chidrenEquals((Globally<T>) o);
	}
}
