package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.parkbench.Experiment;
import ca.uqac.lif.structures.MathList;

public class StateHistoryProvider extends CombinatorialTriagingFunctionProvider<MathList<Integer>>
{
	StateHistoryProvider(AutomatonProvider provider, int strength)
	{
		super(provider, strength);
	}

	@Override
	public void write(Experiment e) 
	{
		super.write(e);
		e.setInput(CombinatorialTriagingFunctionProvider.FUNCTION, "State history");
	}

	@Override
	protected TriagingFunction<AtomicEvent, MathList<Integer>> instantiateFunction(Automaton aut)
	{
		return new StateShallowHistory(aut, m_strength);
	}

}
