package ca.uqac.lif.ecp;

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
	public Integer getClass(Trace<AtomicEvent> trace)
	{
		IdentityVisitor visitor = new IdentityVisitor();
		m_automaton.read(trace, visitor);
		return visitor.m_lastVisited;
	}
	
	protected static class IdentityVisitor extends AutomatonVisitor
	{
		/**
		 * The ID of the last visited state
		 */
		protected int m_lastVisited;
		
		@Override
		public void visit(Vertex<AtomicEvent> start_state, Edge<AtomicEvent> edge)
		{
			if (edge == null)
			{
				// TODO: should be replaced with initial state of the automaton
				// (which is not necessarily 0)
				m_lastVisited = 0;
			}
			m_lastVisited = edge.m_destination;
		}
	}

}
