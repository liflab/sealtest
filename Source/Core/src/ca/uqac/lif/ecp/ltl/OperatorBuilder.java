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
import ca.uqac.lif.util.EmptyException;

/**
 * Builds an expression from some source
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public abstract class OperatorBuilder<T extends Event>
{
	public abstract Operator<T> build() throws BuildException;
	
	public static class BuildException extends EmptyException
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;

		public BuildException(String message)
		{
			super(message);
		}
	}
}
