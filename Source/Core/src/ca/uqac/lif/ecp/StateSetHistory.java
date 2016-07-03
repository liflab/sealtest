package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.StateSetHistory.History;

/**
 * Triaging function where the class of a trace is the set of <i>n</i>-grams
 * found in the sequence of states of the automaton that are visited
 */
public class StateSetHistory extends AutomatonFunction<History> 
{
	/**
	 * The width of the history
	 */
	protected int m_width;

	/**
	 * Instantiates the triaging function
	 * @param a The automaton that is used as the basis for the triaging
	 * @param width The size of the <i>n</i>-grams (i.e. the value of
	 *   <i>n</i>)
	 */
	public StateSetHistory(Automaton a, int width)
	{
		super(a);
		m_width = width;
	}

	@Override
	public History getClass(Trace<AtomicEvent> trace)
	{
		HistoryVisitor visitor = new HistoryVisitor(m_width);
		m_automaton.read(trace, visitor);
		return visitor.m_history;
	}

	protected static class HistoryVisitor extends AutomatonVisitor
	{
		protected History m_history;

		protected int m_width = 1;

		protected List<Integer> m_stateWindow;

		public HistoryVisitor(int width)
		{
			super();
			m_history = new History();
			/*OrderedStateTuple ost = new OrderedStateTuple();
			ost.add(0); // TODO: replace this by initial state
			m_history.add(ost);*/
			m_width = width;
			m_stateWindow = new ArrayList<Integer>(width);
		}

		@Override
		public void visit(Vertex<AtomicEvent> start_state, Edge<AtomicEvent> edge)
		{
			if (edge == null)
			{
				m_stateWindow.add(start_state.getId());
				OrderedStateTuple ost = new OrderedStateTuple(m_stateWindow);
				m_history.add(ost);
				return;
			}
			m_stateWindow.add(edge.m_destination);
			if (m_stateWindow.size() == m_width + 1)
			{
				m_stateWindow.remove(0);
			}
			OrderedStateTuple ost = new OrderedStateTuple(m_stateWindow);
			m_history.add(ost);
		}
	}

	/**
	 * A state tuple is a collection of states
	 * @author Sylvain
	 *
	 */
	public abstract static class StateTuple
	{
		protected List<Integer> m_states;

		public StateTuple()
		{
			super();
			m_states = new ArrayList<Integer>();
		}

		public StateTuple(List<Integer> list)
		{
			this();
			m_states.addAll(list);
		}

		/**
		 * Adds a new state to the tuple
		 * @param state
		 */
		public void add(Integer state)
		{
			m_states.add(state);
		}

		@Override
		public String toString()
		{
			return m_states.toString();
		}
	}

	/**
	 * State tuple where the order where the states appear is important,
	 * i.e. the tuple &lt;1,2,1&gt; is not the same tuple as &lt;1,1,2&gt;
	 */
	public static class OrderedStateTuple extends StateTuple
	{
		public OrderedStateTuple() 
		{
			super();
		}

		public OrderedStateTuple(List<Integer> list)
		{
			super(list);
		}

		@Override
		public int hashCode()
		{
			return m_states.size();
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof OrderedStateTuple))
			{
				return false;
			}
			OrderedStateTuple ost = (OrderedStateTuple) o;
			if (m_states.size() != ost.m_states.size())
			{
				return false;
			}
			for (int state_id : m_states)
			{
				if (!(ost.m_states.contains(state_id)))
				{
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * A state history is a set of state tuples
	 */
	public static class History extends MathSet<StateTuple>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;		
	}

}
