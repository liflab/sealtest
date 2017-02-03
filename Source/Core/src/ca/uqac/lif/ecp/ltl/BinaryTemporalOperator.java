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

import java.util.Iterator;
import java.util.List;

import ca.uqac.lif.ecp.Event;

public abstract class BinaryTemporalOperator<T extends Event> extends NaryOperator<T>
{
	protected List<Operator<T>> m_instantiatedTrees;

	public BinaryTemporalOperator(String symbol)
	{
		super(symbol);
	}
	
	public BinaryTemporalOperator(String symbol, Object ... ops)
	{
		super(symbol, ops);
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		for (Operator<T> op : m_operands)
		{
			op.delete();
		}
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.delete();
		}
	}
	
	@Override
	public void clear()
	{
		super.clear();
		m_instantiatedTrees.clear();
	}
	
	@Override
	public void clean()
	{
		Iterator<Operator<T>> it = m_instantiatedTrees.iterator();
		while (it.hasNext())
		{
			Operator<T> o = it.next();
			if (o.isDeleted())
			{
				it.remove();
			}
			o.clean();
		}
	}
	
}