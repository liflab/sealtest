package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Edge;

/**
 * Triaging function where the class of a trace is the state of
 * the automaton you end on while reading it
 */
public class StateIdentityFunction extends AutomatonFunction<Integer> 
{
	public StateIdentityFunction(Automaton a)
	{
		super(a);
	}

	@Override
	public Integer getStartClass()
	{
		return 0;
	}
	
	@Override
	public Integer processTransition(Edge<AtomicEvent> edge)
	{
		return edge.getDestination();
	}

}
