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
package ca.uqac.lif.ecp.online;

import java.io.PrintStream;

import ca.uqac.lif.ecp.Event;

/**
 * Test hook that simply prints the events it receives.
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class PrintHook<T extends Event> implements TestHook<T,Object>
{
	/**
	 * The print stream where the events will be printed
	 */
	PrintStream m_stream;
	
	/**
	 * The interval (in seconds) that the hook will wait before
	 * the next event is printed 
	 */
	public float m_pauseInterval = 0;
	
	/**
	 * Creates a new print hook that will print its events to the standard
	 * output
	 */
	public PrintHook()
	{
		this(System.out);
	}

	/**
	 * Creates a new print hook that will print its events to a
	 * print stream
	 * @param stream The stream
	 */
	public PrintHook(PrintStream stream)
	{
		super();
		m_stream = stream;
	}
	
	/**
	 * Sets the interval that the hook will wait before
	 * the next event is printed
	 * @param seconds The interval in seconds. Set to 0 or a negative
	 * value for no pause
	 */
	public void setPauseInterval(float seconds)
	{
		m_pauseInterval = seconds;
	}
	
	@Override
	public Object execute(T event) 
	{
		m_stream.println(event);
		if (m_pauseInterval > 0)
		{
			try 
			{
				Thread.sleep((long) (m_pauseInterval * 1000f));
			}
			catch (InterruptedException e) 
			{
				// Do nothing
			}
		}
		return null;
	}

	@Override
	public void reset() 
	{
		m_stream.println("RESET");
	}

	@Override
	public void setDriver(TestDriver<T,Object> driver) 
	{
		// Nothing to do
	}
}
