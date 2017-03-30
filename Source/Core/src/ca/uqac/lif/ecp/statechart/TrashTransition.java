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
 * Special transition representing the move to the "trash" sink, indicating
 * an invalid event was read in the current state of the statechart
 */
public class TrashTransition<T extends Event> extends Transition<T>
{
	@Override
	public boolean matches(T event)
	{
		return true;
	}

	@Override
	public StateNode<T> getTarget() 
	{
		return new StateNode<T>(Statechart.TRASH);
	}

	@Override
	public Transition<T> clone() 
	{
		return this;
	}	
}