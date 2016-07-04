package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.TriagingFunction;

/**
 * Triaging function based on a finite-state automaton
 */
public abstract class AutomatonFunction<U extends Object> implements TriagingFunction<AtomicEvent,U>
{
	protected Automaton m_automaton;
	
	public AutomatonFunction(Automaton a)
	{
		super();
		m_automaton = a;
	}
}
