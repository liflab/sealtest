package ca.uqac.lif.ecp.statechart;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicStatechartBuilder 
{
	protected static final Pattern s_transPattern = Pattern.compile("(.*?) -(.*?)-> (.*?)");
	
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
