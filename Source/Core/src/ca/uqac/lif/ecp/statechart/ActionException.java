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

/**
 * Exception thrown when a problem occurs when executing an action
 * @author Sylvain Hallé
 */
public class ActionException extends Exception
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	public ActionException(Throwable t)
	{
		super(t);
	}
	
	public ActionException(String message)
	{
		super(message);
	}
	
	/**
	 * Exception thrown when a variable assignment refers to an undefined 
	 * variable
	 */
	public static class VariableNotFoundException extends ActionException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public VariableNotFoundException(String var_name)
		{
			super("The variable " + var_name + " cannot be found");
		}		
	}
	
	/**
	 * Exception thrown when the value of a variable goes outside its 
	 * defined bounds 
	 */
	public static class ValueOutOfBoundsException extends ActionException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public ValueOutOfBoundsException(String var_name)
		{
			super("The variable " + var_name + " is assigned a value out of its bounds");
		}		
	}
	
}