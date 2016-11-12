package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.PrefixCategoryClosure;
import ca.uqac.lif.ecp.atomic.ActionShallowHistory;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.structures.MathList;

public class CayleyActionShallowHistoryExperiment extends CayleyAutomatonExperiment<MathList<AtomicEvent>>
{
	public static final String STRENGTH = "strength";
	public static final String ALGORITHM = "algorithm";
	
	/**
	 * The strength of the coverage metric (i.e. the value of <i>t</i>
	 * in "<i>t</i>-way")
	 */
	protected int m_strength;
	
	public CayleyActionShallowHistoryExperiment()
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
	protected CayleyGraph<AtomicEvent,MathList<AtomicEvent>> getCayleyGraph() 
	{
		ActionShallowHistory hist = new ActionShallowHistory(m_automaton, m_strength);
		CayleyGraph<AtomicEvent,MathList<AtomicEvent>> first_graph = hist.getCayleyGraph();
		System.err.println("Size of original CG: " + first_graph.getVertexCount() + " vertices, " + first_graph.getEdgeCount() + " edges");
		PrefixCategoryClosure<AtomicEvent,MathList<AtomicEvent>> closure = new PrefixCategoryClosure<AtomicEvent,MathList<AtomicEvent>>();
		CayleyGraph<AtomicEvent,MathList<AtomicEvent>> second_graph = closure.getClosureGraph(first_graph);
		System.err.println("Size of closure CG: " + second_graph.getVertexCount() + " vertices, " + second_graph.getEdgeCount() + " edges");
		return second_graph;
	}
}
