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
package ca.uqac.lif.ecp;

/**
 * Exception thrown when an unexpected condition occurs. This corresponds to
 * problems that are not supposed to occur in a normal use of the system.
 * @author Sylvain Hallé
 *
 */
public class UnexpectedError extends RuntimeException 
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	public UnexpectedError(Throwable t)
	{
		super(t);
	}
	
	public UnexpectedError(String message)
	{
		super(message);
	}

}
