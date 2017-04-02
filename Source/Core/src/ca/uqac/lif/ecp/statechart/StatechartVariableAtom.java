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
import ca.uqac.lif.ecp.ltl.ConcreteValue;

/**
 * Placeholder in an expression that refers to the value of a state variable
 * of some statechart.
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events in the statechart
 * @param <U> The type of the value of the variable
 */
public class StatechartVariableAtom<T extends Event,U> extends ConcreteValue<T,U> 
{
	/**
	 * The name of the statechart variable
	 */
	protected String m_variableName;
	
	/**
	 * The statechart in which to fetch the value
	 */
	protected Statechart<T> m_statechart; 
	
	/**
	 * Creates a new statechart variable atom
	 * @param var_name The name of the statechart variable
	 * @param statechart The statechart in which to fetch the value
	 */
	public StatechartVariableAtom(String var_name, Statechart<T> statechart)
	{
		super();
		m_variableName = var_name;
		m_statechart = statechart;
	}

	@SuppressWarnings("unchecked")
	@Override
	public U getValue(T structure) 
	{
		return (U) m_statechart.getVariable(m_variableName);
	}
	
	@Override
	public String toString()
	{
		return m_variableName;
	}

}
