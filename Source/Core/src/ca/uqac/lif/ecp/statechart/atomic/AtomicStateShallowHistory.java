package ca.uqac.lif.ecp.statechart.atomic;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.statechart.Configuration;
import ca.uqac.lif.ecp.statechart.StateShallowHistory;
import ca.uqac.lif.ecp.statechart.Statechart;
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
	public CayleyGraph<AtomicEvent,MathList<Configuration<AtomicEvent>>> getCayleyGraph()
	{
		if (m_statechart instanceof AtomicStatechart)
		{
			Alphabet<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
			alphabet.addAll(((AtomicStatechart) m_statechart).getAlphabet());
			AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>>(m_statechart);
			return factory.getGraph(this);
		}
		return null;
	}

}
