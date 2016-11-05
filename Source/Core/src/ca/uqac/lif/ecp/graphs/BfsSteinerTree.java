package ca.uqac.lif.ecp.graphs;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;

public class BfsSteinerTree<T extends Event, U> extends SteinerTree<T, U>
{
	public BfsSteinerTree(CayleyGraph<T, U> graph)
	{
		super(graph);
	}

}
