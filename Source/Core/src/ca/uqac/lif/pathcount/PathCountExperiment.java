package ca.uqac.lif.pathcount;

import ca.uqac.lif.parkbench.Experiment;

public abstract class PathCountExperiment extends Experiment
{
	protected transient Graph m_graph;
	
	PathCountExperiment()
	{
		super();
		describe("property", "Name of the property this automaton represents");
		describe("max-length", "Upper bound on the length of the paths to enumerate");
	}
	
	public PathCountExperiment(String property, int max_length)
	{
		this();
		setInput("property", property);
		setInput("max-length", max_length);
	}
	
	public PathCountExperiment setGraph(Graph g)
	{
		m_graph = g;
		return this;
	}
	
	public Graph getGraph()
	{
		return m_graph;
	}



}
