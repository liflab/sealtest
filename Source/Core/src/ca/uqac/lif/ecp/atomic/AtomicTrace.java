package ca.uqac.lif.ecp.atomic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.Trace;

public class AtomicTrace extends Trace<AtomicEvent> 
{
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
			Trace<AtomicEvent> trace = new Trace<AtomicEvent>();
			String[] labels = line.split(",");
			for (String label : labels)
			{
				AtomicEvent event = new AtomicEvent(label);
				trace.add(event);
			}
			set.add(trace);
		}
		return set;
	}	
}
