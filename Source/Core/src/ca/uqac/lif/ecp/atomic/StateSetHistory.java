package ca.uqac.lif.ecp.atomic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.MathList;
import ca.uqac.lif.ecp.MathSet;

/**
 * Triaging function where the class of a trace is the set of <i>n</i>-grams
 * found in the sequence of states of the automaton that are visited
 */
public class StateSetHistory extends AutomatonFunction<MathSet<Collection<Integer>>> 
{
	/**
	 * The width of the history
	 */
	protected int m_width;
	
	/**
	 * Whether the n-grams should be ordered or not
	 */
	protected boolean m_ordered = true;
	
	/**
	 * The history of n-grams collected so far
	 */
	protected MathSet<Collection<Integer>> m_history;

	/**
	 * A sliding window of the n last state IDs visited
	 */
	protected List<Integer> m_stateWindow;

	/**
	 * Instantiates the triaging function
	 * @param a The automaton that is used as the basis for the triaging
	 * @param width The size of the <i>n</i>-grams (i.e. the value of
	 *   <i>n</i>)
	 * @param ordered Whether the n-grams should be ordered or not
	 */
	public StateSetHistory(Automaton a, int width, boolean ordered)
	{
		super(a);
		m_ordered = ordered;
		m_width = width;
	}

	@Override
	public MathSet<Collection<Integer>> getStartClass()
	{
		return new MathSet<Collection<Integer>>();
	}
	
	@Override
	public MathSet<Collection<Integer>> processTransition(Edge<AtomicEvent> edge)
	{
		m_stateWindow.add(edge.getDestination());
		if (m_stateWindow.size() == m_width + 1)
		{
			m_stateWindow.remove(0);
		}
		Collection<Integer> ost = newCollection();
		ost.addAll(m_stateWindow);
		m_history.add(ost);
		MathSet<Collection<Integer>> out_set = new MathSet<Collection<Integer>>();
		out_set.addAll(m_history);
		return out_set;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_history = new MathSet<Collection<Integer>>();
		m_stateWindow = new ArrayList<Integer>(m_width);		
	}
	
	/**
	 * Gets a new collection of states. Depending on whether the visitor
	 * is ordered or not, a set or a list will be given.
	 * @return The collection
	 */
	protected Collection<Integer> newCollection()
	{
		if (m_ordered)
		{
			return new MathList<Integer>();
		}
		return new MathSet<Integer>();
	}
}
