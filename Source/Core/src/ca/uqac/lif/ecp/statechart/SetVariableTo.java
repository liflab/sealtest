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
 * Action that sets a state variable to a hard value
 * @author Sylvain Hallé
 * @param <T> The type of events in the statechart 
 * @param <U> The type of the state variable to modify
 */
public class SetVariableTo<T extends Event,U> extends VariableAssignment<T,U> 
{
	/**
	 * The value to give to the variable
	 */
	protected final U m_value;
	
	public SetVariableTo(String var_name, U value) 
	{
		super(var_name);
		m_value = value;
	}

	@Override
	public void execute(T event, Transition<T> transition, Statechart<T> statechart) 
	{
		Statechart<?> owner = statechart.findOwner(m_variableName);
		if (owner != null)
		{
			owner.setVariable(m_variableName, m_value);
		}
	}
}
