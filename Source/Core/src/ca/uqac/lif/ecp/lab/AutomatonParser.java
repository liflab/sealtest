package ca.uqac.lif.ecp.lab;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.parkbench.Experiment;

public class AutomatonParser implements AutomatonProvider 
{
	protected Automaton m_automaton;
	
	protected String m_title;
	
	protected static Pattern pat_title = Pattern.compile("Title: (.*)");
	
	public AutomatonParser(Scanner scanner)
	{
		super();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.startsWith("digraph"))
				break;
			Matcher mat = pat_title.matcher(line);
			if (mat.find())
			{
				m_title = mat.group(1).trim();
			}
		}
		m_automaton = Automaton.parseDot(scanner);
	}

	@Override
	public Automaton getAutomaton() 
	{
		return m_automaton;
	}

	@Override
	public void write(Experiment e) 
	{
		e.describe(PROPERTY_NAME, PROPERTY_DESCRIPTION);
		e.setInput(PROPERTY_NAME, m_title);
	}
}
