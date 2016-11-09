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
package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Event;

public class StringAtom extends Event
{
	private final String x;
	
	public StringAtom()
	{
		this("");
	}
	
	public StringAtom(String x)
	{
		super();
		this.x = x;
	}
	
	@Override
	public int hashCode()
	{
		return x.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof StringAtom && ((StringAtom) o).x.compareTo(this.x) == 0;
	}
	
	@Override
	public String toString()
	{
		return "\"" + x + "\"";
	}
}