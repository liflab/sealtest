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

public class Constant<T,U> extends ConcreteValue<T,U> 
{
	/**
	 * The value
	 */
	protected final U m_value;
	
	/**
	 * Creates a new constant
	 * @param value The value of this constant
	 */
	public Constant(U value)
	{
		super();
		m_value = value;
	}
	
	@Override
	public final U getValue(T structure)
	{
		return m_value;
	}
}
