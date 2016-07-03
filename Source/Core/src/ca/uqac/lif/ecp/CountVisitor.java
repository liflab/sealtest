package ca.uqac.lif.ecp;

import java.util.ArrayList;

import ca.uqac.lif.ecp.lab.GraphExperiment;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;

public class CountVisitor<T extends Event,U extends Object> extends BreadthFirstVisitor<T,U>
{
	protected GraphExperiment m_experiment;
	
	public CountVisitor(GraphExperiment experiment)
	{
		super();
		m_experiment = experiment;
	}
	
	public void start(CayleyGraph<T,U> g, int start_id, int max_depth)
	{
		super.start(g, start_id, max_depth);
	}

	@Override
	public void visit(ArrayList<Edge<T>> path)
	{
		int length = path.size();
		Edge<T> last_edge = path.get(path.size() - 1);
		int last_label = last_edge.m_destination;
		JsonList list = m_experiment.readList(Integer.toString(last_label));
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
