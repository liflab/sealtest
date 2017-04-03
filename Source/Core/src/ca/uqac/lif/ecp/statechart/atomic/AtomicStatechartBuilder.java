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
package ca.uqac.lif.ecp.statechart.atomic;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.statechart.Configuration;
import ca.uqac.lif.ecp.statechart.NestedState;
import ca.uqac.lif.ecp.statechart.State;
import ca.uqac.lif.ecp.statechart.Statechart;

/**
 * Creates a statechart of atomic events out of a text string. The statechart
 * must be structured as follows.
 * <ul>
 * <li>The statechart is enclosed between the lines <tt>begin statechart</tt>
 * and <tt>end statechart</tt></li>
 * <li>The states are declared in a section <tt>begin states</tt> &hellip;
 * <tt>end states</tt></li>
 * <li>The transitions are declared in a section <tt>begin transitions</tt> &hellip;
 * <tt>end transitions</tt></li>
 * </ul> 
 * @author Sylvain Hallé
 */
public class AtomicStatechartBuilder 
{
	/**
	 * The pattern to parse transitions
	 */
	protected static final Pattern s_transPattern = Pattern.compile("(.*?) -(.*?)-> (.*?)");
	
	/**
	 * Parses a statechart from a source of text
	 * @param scanner A scanner open on a source of text
	 * @return The statechart read from the source
	 * @throws ParseException Thrown if the source of text does not follow the
	 *   expected syntax
	 */
	public static Statechart<AtomicEvent> parseFromString(Scanner scanner) throws ParseException
	{
		String line = nextValidLine(scanner);
		if (line == null)
		{
			throw new ParseException("Input contains no valid lines");
		}
		if (line.startsWith("begin statechart"))
		{
			AtomicStatechart new_as = new AtomicStatechart();
			new_as = parseStatechart(new_as, scanner);
			return new_as;
		}
		throw new ParseException("Parsing failed");
	}
	
	/**
	 * Parses a statechart from a source of text
	 * @param sc The statechart to "fill" with the contents to be read
	 * @param scanner A scanner pointing to the first line of the internal
	 * contents of the statechart
	 * @return The parsed statechart
	 * @throws ParseException Thrown if the source of text does not follow the
	 *   expected syntax
	 */
	protected static AtomicStatechart parseStatechart(AtomicStatechart sc, Scanner scanner) throws ParseException
	{
		String line = nextValidLine(scanner);
		if (line == null || !line.startsWith("begin states"))
			return null;
		parseStates(sc, scanner);
		line = nextValidLine(scanner);
		if (line == null || !line.startsWith("begin transitions"))
			return null;
		parseTransitions(sc, scanner);
		line = nextValidLine(scanner);
		if (line != null && line.startsWith("end statechart"))
			return sc;
		return null;
	}

	/**
	 * Parses the states of a statechart
	 * @param sc The statechart to "fill" with the contents to be read
	 * @param scanner A scanner pointing to the first line of the list of
	 * states of the statechart
	 * @throws ParseException Thrown if the source of text does not follow the
	 *   expected syntax
	 */
	protected static void parseStates(Statechart<AtomicEvent> sc, Scanner scanner) throws ParseException
	{
		String line = nextValidLine(scanner);
		while (line != null && !line.startsWith("end states"))
		{
			if (!line.contains("="))
			{
				// This is an atomic state
				State<AtomicEvent> s = new State<AtomicEvent>(line);
				sc.add(s);
				line = nextValidLine(scanner);
			}
			else
			{
				String[] parts = line.split("=");
				String state_name = parts[0].trim();
				if (state_name.isEmpty())
				{
					throw new ParseException("Empty state name");
				}
				if (line.contains("begin statechart"))
				{
					// This is a nested state
					NestedState<AtomicEvent> ns = new NestedState<AtomicEvent>(state_name);
					AtomicStatechart new_as = new AtomicStatechart();
					new_as = parseStatechart(new_as, scanner);
					ns.addStatechart(new_as);
					sc.add(ns);
					line = nextValidLine(scanner);
				}
				else if (line.contains("begin parallel"))
				{
					// This is a set of orthogonal regions
					NestedState<AtomicEvent> ns = new NestedState<AtomicEvent>(state_name);
					String in_line = nextValidLine(scanner); // begin statechart
					while (!in_line.startsWith("end parallel"))
					{
						AtomicStatechart new_as = new AtomicStatechart();
						new_as = parseStatechart(new_as, scanner);
						if (new_as == null)
						{
							throw new ParseException("Expected a statechart; parsed nothing");
						}
						ns.addStatechart(new_as);
						sc.add(ns);
						in_line = nextValidLine(scanner);
					}
					line = nextValidLine(scanner);
				}
				else
				{
					throw new ParseException("Incorrect state definition");
				}
			}
		}
	}
	
	/**
	 * Parses the transitions of a statechart
	 * @param sc The statechart to "fill" with the contents to be read
	 * @param scanner A scanner pointing to the first line of the list of
	 * transitions of the statechart
	 * @throws ParseException Thrown if the source of text does not follow the
	 *   expected syntax
	 */
	protected static void parseTransitions(Statechart<AtomicEvent> sc, Scanner scanner) throws ParseException
	{
		String line = nextValidLine(scanner);
		while (line != null && !line.startsWith("end transitions"))
		{
			Matcher mat = s_transPattern.matcher(line);
			if (!mat.matches())
			{
				throw new ParseException("Invalid transition definition");
			}
			String state_source = mat.group(1).trim();
			String trans_label = mat.group(2).trim();
			String trans_target = mat.group(3).trim();
			Configuration<AtomicEvent> target = parseTarget(trans_target);
			AtomicTransition trans = new AtomicTransition(new AtomicEvent(trans_label), target);
			sc.add(state_source, trans);
			line = nextValidLine(scanner);
		}
	}
	
	/**
	 * Creates a target configuration from a string of text
	 * @param target_string The string of text
	 * @return The parsed configuration
	 */
	protected static Configuration<AtomicEvent> parseTarget(String target_string)
	{
		String[] parts = target_string.split(",");
		Configuration<AtomicEvent> child = null;
		for (int i = parts.length - 1; i >=0; i--)
		{
			String part = parts[i];
			Configuration<AtomicEvent> cur_node = null;
			if (part.compareTo("..") == 0)
			{
				cur_node = new Configuration.UpStateNode<AtomicEvent>(child);
			}
			else
			{
				cur_node = new Configuration<AtomicEvent>(part);
				if (child != null)
				{
					cur_node.addChild(child);
				}
			}
			child = cur_node;
		}
		return child;
	}
	
	/**
	 * Gets the next valid line from the input source. A line if valid if
	 * does not contain only whitespace, and whose first non-whitespace
	 * character is not "#"
	 * @param scanner A scanner open on a source of text
	 * @return The next valid line, or {@code null} if there are no more
	 * valid lines in the input source
	 */
	protected static String nextValidLine(Scanner scanner)
	{
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			return line;
		}
		return null;
	}
}
