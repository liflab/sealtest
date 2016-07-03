package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Triaging function where the class of a trace is the set of <i>n</i>-grams
 * found in the sequence of transitions of the automaton that are visited
 */
public class EdgeSetHistory extends AutomatonFunction<MathSet<Collection<Edge<AtomicEvent>>>> 
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
	 * Instantiates the triaging function
	 * @param a The automaton that is used as the basis for the triaging
	 * @param width The size of the <i>n</i>-grams (i.e. the value of
	 *   <i>n</i>)
	 * @param ordered Whether the n-grams should be ordered or not
	 */
	public EdgeSetHistory(Automaton a, int width, boolean ordered)
	{
		super(a);
		m_ordered = ordered;
		m_width = width;
	}

	@Override
	public MathSet<Collection<Edge<AtomicEvent>>> getClass(Trace<AtomicEvent> trace)
	{
		HistoryVisitor visitor = new HistoryVisitor(m_width);
		m_automaton.read(trace, visitor);
		return visitor.m_history;
	}

	protected class HistoryVisitor extends AutomatonVisitor
	{
		protected MathSet<Collection<Edge<AtomicEvent>>> m_history;

		protected List<Edge<AtomicEvent>> m_stateWindow;

		public HistoryVisitor(int width)
		{
			super();
			m_history = new MathSet<Collection<Edge<AtomicEvent>>>();
			m_stateWindow = new ArrayList<Edge<AtomicEvent>>(width);
		}

		@Override
		public void visit(Vertex<AtomicEvent> start_state, Edge<AtomicEvent> edge)
		{
			if (edge == null)
			{
				return;
			}
			m_stateWindow.add(edge);
			if (m_stateWindow.size() == m_width + 1)
			{
				m_stateWindow.remove(0);
			}
			Collection<Edge<AtomicEvent>> ost = newCollection();
			ost.addAll(m_stateWindow);
			m_history.add(ost);
		}
		
		/**
		 * Gets a new collection of states. Depending on whether the visitor
		 * is ordered or not, a set or a list will be given.
		 * @return The collection
		 */
		protected Collection<Edge<AtomicEvent>> newCollection()
		{
			if (m_ordered)
			{
				return new MathList<Edge<AtomicEvent>>();
			}
			return new MathSet<Edge<AtomicEvent>>();
		}
	}
}
