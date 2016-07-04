package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.Vertex;

/**
 * Triaging function based on a finite-state automaton
 */
public abstract class AutomatonFunction<U extends Object> extends TriagingFunction<AtomicEvent,U>
{
	protected Automaton m_automaton;
	
	protected Vertex<AtomicEvent> m_currentVertex;
	
	public AutomatonFunction(Automaton a)
	{
		super();
		m_automaton = a;
		reset();
	}
	
	@Override
	public void reset()
	{
		// TODO: don't hardcode initial state
		m_currentVertex = m_automaton.getVertex(0); 
	}
	
	@Override
	public U read(AtomicEvent e)
	{
		Edge<AtomicEvent> edge = m_automaton.getTransition(m_currentVertex, e);
		m_currentVertex = m_automaton.getVertex(edge.getDestination());
		assert m_currentVertex != null;
		return processTransition(edge);
	}
	
	public abstract U processTransition(Edge<AtomicEvent> edge);
}
