package ca.uqac.lif.ecp.atomic;

import java.util.Set;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CayleyGraphFactory;

/**
 * Class that creates a Cayley graph out of a triaging function based on a
 * finite-state automaton.
 * @param <U> The output type of the triaging function
 */
public class AutomatonCayleyGraphFactory<U extends Object> extends CayleyGraphFactory<AtomicEvent,U>
{
	protected Alphabet<AtomicEvent> m_alphabet;
	
	public AutomatonCayleyGraphFactory(Alphabet<AtomicEvent> alphabet)
	{
		super();
		m_alphabet = alphabet;
	}

	@Override
	protected Set<AtomicEvent> getNextEvents() 
	{
		return m_alphabet;
	}

}
