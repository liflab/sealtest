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

import ca.uqac.lif.ecp.Event;

/**
 * Top-level class for all LTL connectives and operators
 * @param <T> The type of the events 
 * @author Sylvain Hallé
 */
public abstract class Operator<T extends Event>
{
	/**
	 * Truth value
	 */
	public enum Value {TRUE, FALSE, INCONCLUSIVE};
	
	/**
	 * The current value that this tree holds
	 */
	protected Value m_value = Value.INCONCLUSIVE;
	
	/**
	 * Evaluates the operator on the new event
	 * @param event The event
	 */
	public abstract void evaluate(T event);
	
	/**
	 * Gets the current truth value of the operator
	 * @return The current truth value of the operator
	 */
	public final Value getValue()
	{
		return m_value;
	}
	
	public abstract Operator<T> copy(boolean with_tree);
	
	protected void copyInto(Operator<T> o, boolean with_tree)
	{
		if (with_tree)
		{
			o.m_value = m_value;
		}
	}
	
	
}
