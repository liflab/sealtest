/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hall�

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

import java.util.Random;

/**
 * Trace generator that picks events randomly
 * 
 * @param <T> The type of the events in the traces that are generated
 */
public abstract class RandomTraceGenerator<T extends Event> extends TraceGenerator<T>
{
	/**
	 * A random number generator used to generate the random events
	 */
	protected Random m_random;

	/**
	 * Generates a new event randomly
	 * @param random The random number generator used to generate the event
	 * @return A new event
	 */
	protected abstract T nextEvent(Random random);
	
	public RandomTraceGenerator(Random random)
	{
		super();
		m_random = random;
	}
}
