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
package ca.uqac.lif.ecp.atomic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.Trace;

public class AtomicEvent extends Event 
{
	/**
	 * The event's label
	 */
	private final String m_label;
	
	/**
	 * Creates a new atomic event with given label
	 * @param label The label
	 */
	public AtomicEvent(String label)
	{
		super();
		m_label = label;
	}
	
	/**
	 * Gets the event's label
	 * @return The label
	 */
	public String getLabel()
	{
		return m_label;
	}
	
	@Override
	public String toString()
	{
		return m_label;
	}
	
	@Override
	public int hashCode()
	{
		return m_label.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null || !(o instanceof AtomicEvent))
		{
			return false;
		}
		return m_label.compareTo(((AtomicEvent) o).m_label) == 0;
	}
	
	/**
	 * Creates a set of traces from an external source.
	 * The source must be a set of text lines.
	 * @param scanner The scanner to the text lines
	 * @return The set of traces
	 */
	public static Set<Trace<AtomicEvent>> readTestSuite(Scanner scanner)
	{
		Set<Trace<AtomicEvent>> test_suite = new HashSet<Trace<AtomicEvent>>();
		Map<String,AtomicEvent> event_pool = new HashMap<String,AtomicEvent>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			Trace<AtomicEvent> trace = new Trace<AtomicEvent>();
			String[] parts = line.split(",");
			for (String part : parts)
			{
				String name = part.trim();
				if (event_pool.containsKey(name))
				{
					// Re-use from event pool
					trace.add(event_pool.get(name));
				}
				else
				{
					AtomicEvent ae = new AtomicEvent(name);
					event_pool.put(name, ae);
					trace.add(ae);
				}
			}
			test_suite.add(trace);
		}
		return test_suite;
	}
}
