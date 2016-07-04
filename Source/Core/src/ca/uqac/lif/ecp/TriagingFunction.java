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

public abstract class TriagingFunction<T,U> 
{
	/**
	 * Gets the equivalence class associated with a given trace
	 * @param The trace to read
	 * @return The equivalence class
	 */
	public U getClass(Trace<T> trace)
	{
		reset();
		U last = getStartClass();
		for (T event : trace)
		{
			last = read(event);
		}
		return last;
	}
	
	/**
	 * Resets the triaging function to its initial state
	 */
	public void reset()
	{
		// Do nothing
	}
	
	/**
	 * Reads the next event of the trace
	 * @param event The event
	 * @return The equivalence class that the trace read since
	 *   the last call to {@link #reset()} belongs to
	 */
	public abstract U read(T event);
	
	/**
	 * Gets the equivalence class for the empty trace
	 * @return The class
	 */
	public abstract U getStartClass();
}
