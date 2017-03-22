package ca.uqac.lif.ecp.statechart;

import java.util.List;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyGraphFactory;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.graphs.Vertex;

public class AtomicStatechartCayleyGraphFactory extends CayleyGraphFactory<AtomicEvent,List<Integer>>
{
	/**
	 * The set of atomic events that will be used as the alphabet
	 */
	protected Set<AtomicEvent> m_possibleEvents;
	
	public AtomicStatechartCayleyGraphFactory(AtomicStatechart s)
	{
		super();
		m_possibleEvents = s.getAlphabet();
	}
	
	@Override
	protected Set<AtomicEvent> getNextEvents(Vertex<AtomicEvent> vertex) 
	{
		return m_possibleEvents;
	}

}
