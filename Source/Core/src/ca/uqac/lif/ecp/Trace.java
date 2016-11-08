/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hall�

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
package ca.uqac.lif.ecp;

import java.util.LinkedList;
import java.util.List;

public class Trace<T> extends LinkedList<T>
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	public Trace()
	{
		super();
	}
	
	public Trace(Trace<T> t)
	{
		this();
		addAll(t);
	}

	public Trace<T> suffixFrom(int index)
	{
		Trace<T> out = new Trace<T>();
		List<T> out_list = subList(index, size());
		out.addAll(out_list);
		return out;
	}
	
	@Override
	public int hashCode()
	{
		return size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Trace<?>))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		Trace<T> t = (Trace<T>) o;
		return equals(t);
	}
	
	/**
	 * Checks if two traces are equal. This is the case when
	 * all their events are pairwise equal.
	 * @param t The other trace
	 * @return true if traces are equal, false otherwise
	 */
	private boolean equals(Trace<T> t)
	{
		if (t.size() != size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			T e1 = get(i);
			T e2 = t.get(i);
			if (!e1.equals(e2))
			{
				return false;
			}
		}
		return true;
	}
}
