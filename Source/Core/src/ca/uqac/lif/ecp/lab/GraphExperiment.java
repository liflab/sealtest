package ca.uqac.lif.ecp.lab;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.AtomicCayleyGraph;
import ca.uqac.lif.parkbench.Experiment;

public abstract class GraphExperiment extends Experiment
{
	protected transient AtomicCayleyGraph m_graph;
	
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
		AtomicCayleyGraph g = AtomicCayleyGraph.parseDot(scanner);
		m_graph = g;
	}
	
	public GraphExperiment setGraph(AtomicCayleyGraph g)
	{
		m_graph = g;
		return this;
	}
	
	public AtomicCayleyGraph getGraph()
	{
		return m_graph;
	}
}
