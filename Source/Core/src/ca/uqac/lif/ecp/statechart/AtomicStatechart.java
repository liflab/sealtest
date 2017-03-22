package ca.uqac.lif.ecp.statechart;

import java.util.Set;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicStatechart extends Statechart<AtomicEvent> 
{
	/**
	 * The alphabet associated to this statechart
	 */
	protected Alphabet<AtomicEvent> m_alphabet;
	
	/**
	 * Gets the set of all the events that occur as transition labels in this
	 * statechart
	 * @return The set of events
	 */
	@SuppressWarnings("unchecked")
	public Set<AtomicEvent> getAlphabet()
	{
		Set<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
		for (Set<Transition<AtomicEvent>> trans : m_transitions.values())
		{
			for (Transition<AtomicEvent> t : trans)
			{
				alphabet.add(((AtomicTransition) t).getEvent());
			}
		}
		for (State s : m_states.values())
		{
			if (s instanceof BoxState)
			{
				alphabet.addAll(((AtomicStatechart) ((BoxState<AtomicEvent>) s).m_contents).getAlphabet());
			}
		}
		return alphabet;
	}
}
