package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.PrefixCategoryClosure;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.structures.MathList;

public class CayleyStateShallowHistoryExperiment extends CayleyAutomatonExperiment<MathList<Integer>>
{
	public static final String STRENGTH = "strength";
	public static final String ALGORITHM = "algorithm";
	
	/**
	 * The strength of the coverage metric (i.e. the value of <i>t</i>
	 * in "<i>t</i>-way")
	 */
	protected int m_strength;
	
	public CayleyStateShallowHistoryExperiment()
	{
		super();
		describe(STRENGTH, "The strength of the coverage metric");
		describe(ALGORITHM, "The algorithm used to generate the test suite");
		setInput(ALGORITHM, "Category Coverage");
	}
	
	public void setStrength(int strength)
	{
		m_strength = strength;
		setInput(STRENGTH, m_strength);
	}

	@Override
	protected CayleyGraph<AtomicEvent,MathList<Integer>> getCayleyGraph() 
	{
		StateShallowHistory hist = new StateShallowHistory(m_automaton, m_strength);
		CayleyGraph<AtomicEvent,MathList<Integer>> first_graph = hist.getCayleyGraph();
		PrefixCategoryClosure<AtomicEvent,MathList<Integer>> closure = new PrefixCategoryClosure<AtomicEvent,MathList<Integer>>();
		CayleyGraph<AtomicEvent,MathList<Integer>> second_graph = closure.getClosureGraph(first_graph);
		return second_graph;
	}
}
