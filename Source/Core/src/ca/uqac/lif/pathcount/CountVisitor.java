package ca.uqac.lif.pathcount;

import java.util.ArrayList;

import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;

public class CountVisitor extends BreadthFirstVisitor
{
	protected PathCountExperiment m_experiment;
	
	public CountVisitor(PathCountExperiment experiment)
	{
		super();
		m_experiment = experiment;
	}
	
	public void start(Graph g, String start_label, int max_depth)
	{
		super.start(g, start_label, max_depth);
	}

	@Override
	public void visit(ArrayList<Edge> path)
	{
		int length = path.size();
		Edge last_edge = path.get(path.size() - 1);
		String last_label = last_edge.m_destination;
		JsonList list = m_experiment.readList(last_label);
		JsonNumber j_num = (JsonNumber) list.get(length);
		list.set(length, new JsonNumber(j_num.numberValue().intValue() + 1));
	}
	
	@Override
	public void depthStep(int depth)
	{
		float progression = (float) depth / m_experiment.readFloat("max-length");
		System.out.println("DEPTH " + depth + " " + progression);
		m_experiment.setProgression(progression); 
	}
}
