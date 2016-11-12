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
package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Event;

public class AtomicEvent extends Event 
{
	/**
	 * The event's label
	 */
	private final String m_label;
	
	/**
	 * Creates a new atomic event with given label
	 * @param label The label
	 */
	public AtomicEvent(String label)
	{
		super();
		m_label = label;
	}
	
	/**
	 * Gets the event's label
	 * @return The label
	 */
	public String getLabel()
	{
		return m_label;
	}
	
	@Override
	public String toString()
	{
		return m_label;
	}
	
	@Override
	public int hashCode()
	{
		return m_label.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null || !(o instanceof AtomicEvent))
		{
			return false;
		}
		return m_label.compareTo(((AtomicEvent) o).m_label) == 0;
	}
}
