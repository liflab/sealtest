package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Edge;

public class AtomicEdge extends Edge<AtomicEvent> 
{
	public AtomicEdge(int source, AtomicEvent label, int destination) 
	{
		super(source, label, destination);
	}

}
