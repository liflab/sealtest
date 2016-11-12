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
package ca.uqac.lif.ecp.atomic;

/**
 * Event representing the "else" case in a finite-state automaton.
 * This is a special event that matches all events that are not
 * already covered by another outgoing edge from a given state.
 * 
 * @author Sylvain Hallé
 */
public class ElseEvent extends AtomicEvent
{
	/**
	 * A single instance of this event
	 */
	public static final ElseEvent instance = new ElseEvent();
	
	/**
	 * The label used to designate such an event
	 */
	public static final String label = "*";

	private ElseEvent()
	{
		super("*");
	}
}
