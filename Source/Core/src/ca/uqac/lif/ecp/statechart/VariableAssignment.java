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
 * Action that modifies the value of a state variable in a statechart
 * @author Sylvain Hallé
 *
 * @param <T> The type of events in the statechart 
 * @param <U> The type of the state variable to modify
 */
public abstract class VariableAssignment<T extends Event,U> extends Action<T> 
{
	/**
	 * The name of the variable to modify
	 */
	protected final String m_variableName;
	
	/**
	 * Creates a new variable assignment
	 * @param var_name The name of the state variable to modify
	 */
	public VariableAssignment(String var_name)
	{
		super();
		m_variableName = var_name;
	}
}
