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
package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;

/**
 * Action that decrements a state variable by a fixed value. The corresponding
 * state variable in the statechart must be of integer type.
 * @author Sylvain Hallé
 * @param <T> The type of events in the statechart 
 */
public class DecrementVariableBy<T extends Event> extends VariableAssignment<T,Integer> 
{
	/**
	 * The value to give to the variable
	 */
	protected final int m_increment;

	/**
	 * The minimum value the variable is allowed to take. Attempting to
	 * decrement the variable below this value will cause the action to fail,
	 * sending the statechart into the trash state.
	 */
	protected final int m_minValue;

	public DecrementVariableBy(String var_name, int increment)
	{
		this(var_name, increment, 100000);
	}

	public DecrementVariableBy(String var_name, int increment, int min_value)
	{
		super(var_name);
		m_increment = increment;
		m_minValue = min_value;
	}

	@Override
	public void execute(T event, Transition<T> transition, Statechart<T> statechart) throws ActionException
	{
		Statechart<?> owner = statechart.findOwner(m_variableName);
		if (owner == null)
		{
			throw new ActionException.VariableNotFoundException(m_variableName);
		}
		int new_value = (Integer) owner.getVariable(m_variableName) - m_increment;
		if (new_value < m_minValue)
		{
			throw new ActionException.ValueOutOfBoundsException(m_variableName);
		}
		owner.setVariable(m_variableName, new_value);
	}
	
	@Override
	public String toString()
	{
		return m_variableName + " -= " + m_increment;
	}
}
