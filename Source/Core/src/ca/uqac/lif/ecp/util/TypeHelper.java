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
package ca.uqac.lif.ecp.util;

/**
 * Provides utility methods to manipulate objects of various types
 * @author Sylvain Hallé
 *
 */
public class TypeHelper 
{
	/**
	 * This class is not meant to be instantiated
	 */
	private TypeHelper()
	{
		super();
	}
	
	/**
	 * Compares two objects based on their values. The method uses the
	 * following rules:
	 * <ol>
	 * <li>Two nulls are equal</li>
	 * <li>If <code>o1</code> and <code>o2</code> are both strings,
	 * they are equal if their text is equal</li>
	 * <li>If <code>o1</code> and <code>o2</code> are both numbers,
	 * they are equal if their conversion into a <code>double</code>
	 * returns the same value</li>
	 * <li>Otherwise, the default {@code equals()} method is invoked</li>
	 * </ol>
	 * @param o1 The first object
	 * @param o2 The second object
	 * @return {@code true} or {@code false} according to the rules
	 * mentioned above
	 */
	public static boolean valueEquals(Object o1, Object o2)
	{
		if (o1 == null && o2 == null)
		{
			return true;
		}
		if ((o1 != null && o2 == null) || (o1 == null && o2 != null))
		{
			return false;
		}
		if (o1 instanceof String && o2 instanceof String)
		{
			return ((String) o1).compareTo((String) o2) == 0;
		}
		if (o1 instanceof Number && o2 instanceof Number)
		{
			return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
		}
		return o1.equals(o2);
	}
	
	/**
	 * Attempts to create a {@code double} number out of an object.
	 * <ul>
	 * <li>If {@code o} is a {@code Number}, it returns {@code o.doubleValue()}</li> 
	 * <li>If {@code o} is a {@code String}, it is parsed as a {@code double}; 0 is
	 * returned if the parsing fails</li>
	 * <li>If {@code o} is anything else, 0 is returned</li>
	 * </ul>
	 * @param o The object
	 * @return A number, created according to the rules above
	 */
	public static double castToDouble(Object o)
	{
		if (o == null)
		{
			return 0;
		}
		if (o instanceof Number)
		{
			return ((Number) o).doubleValue();
		}
		if (o instanceof String)
		{
			try
			{
				return Double.parseDouble((String) o);
			}
			catch (NumberFormatException e)
			{
				// Do nothing
			}
		}
		return 0;
	}
	
	/**
	 * Attempts to create an {@code int} number out of an object.
	 * <ul>
	 * <li>If {@code o} is a {@code Number}, it returns {@code o.intValue()}</li> 
	 * <li>If {@code o} is a {@code String}, it is parsed as an {@code int}; 0 is
	 * returned if the parsing fails</li>
	 * <li>If {@code o} is anything else, 0 is returned</li>
	 * </ul>
	 * @param o The object
	 * @return A number, created according to the rules above
	 */
	public static int castToInteger(Object o)
	{
		if (o == null)
		{
			return 0;
		}
		if (o instanceof Number)
		{
			return ((Number) o).intValue();
		}
		if (o instanceof String)
		{
			try
			{
				return Integer.parseInt((String) o);
			}
			catch (NumberFormatException e)
			{
				// Do nothing
			}
		}
		return 0;
	}
}
