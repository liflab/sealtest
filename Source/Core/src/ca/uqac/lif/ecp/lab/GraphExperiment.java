package ca.uqac.lif.ecp.lab;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.parkbench.Experiment;

public abstract class GraphExperiment extends Experiment
{
	protected transient Automaton m_graph;
	
	GraphExperiment()
	{
		super();
		describe("property", "Name of the property this automaton represents");
	}
	
	public GraphExperiment(Scanner scanner)
	{
		this();
		Pattern pat_title = Pattern.compile("Title: (.*)");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.startsWith("digraph"))
				break;
			Matcher mat = pat_title.matcher(line);
			if (mat.find())
			{
				setInput("property", mat.group(1));
			}
		}
		Automaton g = Automaton.parseDot(scanner);
		m_graph = g;
	}
	
	public GraphExperiment setGraph(Automaton g)
	{
		m_graph = g;
		return this;
	}
	
	public Automaton getGraph()
	{
		return m_graph;
	}
}
