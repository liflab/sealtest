package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.structures.MathList;

public class AtomicStateShallowHistory extends StateShallowHistory<AtomicEvent>
{

	public AtomicStateShallowHistory(Statechart<AtomicEvent> a)
	{
		super(a);
	}
	
	public AtomicStateShallowHistory(Statechart<AtomicEvent> a, int i)
	{
		super(a, i);
	}

	@Override
	public CayleyGraph<AtomicEvent,MathList<StateNode<AtomicEvent>>> getCayleyGraph()
	{
		if (m_automaton instanceof AtomicStatechart)
		{
			Alphabet<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
			alphabet.addAll(((AtomicStatechart) m_automaton).getAlphabet());
			AtomicStatechartCayleyGraphFactory<MathList<StateNode<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<StateNode<AtomicEvent>>>(m_automaton);
			return factory.getGraph(this);
		}
		return null;
	}

}
