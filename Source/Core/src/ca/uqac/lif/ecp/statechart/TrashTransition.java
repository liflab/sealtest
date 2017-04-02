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
import ca.uqac.lif.ecp.statechart.ActionException.ValueOutOfBoundsException;

/**
 * Special transition representing the move to the "trash" sink, indicating
 * an invalid event was read in the current state of the statechart
 */
public class TrashTransition<T extends Event> extends Transition<T>
{
	/**
	 * If the trash transition was taken due to the occurrence of an
	 * exception, store the exception here
	 */
	protected final Exception m_exception;
	
	public TrashTransition(Exception e)
	{
		super();
		m_exception = e;
	}
	
	public TrashTransition()
	{
		this(null);
	}
	
	@Override
	public Configuration<T> getTarget() 
	{
		return new Configuration<T>(Statechart.TRASH);
	}

	@Override
	public Transition<T> clone() 
	{
		return this;
	}

	@Override
	protected boolean afterGuard(T event, Statechart<T> statechart) 
	{
		return true;
	}

	/**
	 * Gets the exception that provoked the firing of this transition
	 * @return The exception, or {@code null} if no exception is
	 * associated to this transition
	 */
	public Exception getException() 
	{
		return m_exception;
	}	
}