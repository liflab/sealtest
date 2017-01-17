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

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.Trace;

/**
 * An ordered sequence of atomic events
 * @author Sylvain Hallé
 */
public class AtomicTrace extends Trace<AtomicEvent> 
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The OS-dependent line separator 
	 */
	protected static final String CRLF = System.getProperty("line.separator");
	
	/**
	 * Reads a set of traces from a text input source. The source must be
	 * made of text structured as follows:
	 * <ul>
	 * <li>Each trace is on one line</li>
	 * <li>Events are separated by commas</li>
	 * <li>Any leading or trailing whitespace is trimmed for each event</li>
	 * <li>Empty lines, and lines beginning by "#" are ignored</li> 
	 * </ul>
	 * Obviously, this method of reading traces only works for reasonably
	 * small, static sets.
	 * 
	 * @param scanner A scanner open on the source to read
	 * @return The set of traces read from the scanner
	 */
	public static Set<Trace<AtomicEvent>> readSet(Scanner scanner)
	{
		Set<Trace<AtomicEvent>> set = new HashSet<Trace<AtomicEvent>>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				// Ignore this line
				continue;
			}
			set.add(readTrace(line));
		}
		return set;
	}	
	
	/**
	 * Reads a trace from a text string
	 * @see #readSet(Scanner)
	 * @param line The string
	 * @return The trace
	 */
	public static Trace<AtomicEvent> readTrace(String line)
	{
		Trace<AtomicEvent> trace = new Trace<AtomicEvent>();
		String[] labels = line.split(",");
		for (String label : labels)
		{
			AtomicEvent event = new AtomicEvent(label);
			trace.add(event);
		}
		return trace;
	}
}
