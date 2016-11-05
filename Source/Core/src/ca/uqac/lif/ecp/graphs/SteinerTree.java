package ca.uqac.lif.ecp.graphs;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;

public abstract class SteinerTree<T extends Event, U> extends CayleyGraphSolver<T, U> 
{

	public SteinerTree(CayleyGraph<T, U> graph) 
	{
		super(graph);
	}

}
