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

public class HologramComposition<T extends Event> extends HologramTransformation<T>
{
	List<HologramTransformation<T>> m_chain;
	
	public HologramComposition()
	{
		super();
		m_chain = new ArrayList<HologramTransformation<T>>();
	}
	
	public HologramComposition(Object ... compositions)
	{
		this();
		m_chain = new ArrayList<HologramTransformation<T>>();
		for (Object c : compositions)
		{
			if (c instanceof HologramComposition)
			{
				@SuppressWarnings("unchecked")
				HologramTransformation<T> comp = (HologramTransformation<T>) c;
				m_chain.add(comp);
			}
		}
	}
	
	/**
	 * Adds a transformation to the composition
	 * @param t
	 */
	public void add(HologramTransformation<T> t)
	{
		m_chain.add(t);
	}

	@Override
	public Operator<T> transform(Operator<T> tree)
	{
		for (HologramTransformation<T> trans : m_chain)
		{
			tree = trans.transform(tree);
		}
		return tree;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (HologramTransformation<T> comp : m_chain)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.append(" + ");
			}
			out.append(comp);
		}
		return out.toString();
	}

}
