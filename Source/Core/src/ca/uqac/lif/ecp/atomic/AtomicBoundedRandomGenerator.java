package ca.uqac.lif.ecp.atomic;

import java.util.Random;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.BoundedRandomTraceGenerator;

public class AtomicBoundedRandomGenerator extends BoundedRandomTraceGenerator<AtomicEvent> 
{
	/**
	 * The alphabet to pick events from
	 */
	protected AtomicEvent[] m_alphabet;
	
	public AtomicBoundedRandomGenerator(Random random, int num_traces, int trace_length, Alphabet<AtomicEvent> alphabet) 
	{
		super(random, num_traces, trace_length);
		m_alphabet = alphabet.toArray(new AtomicEvent[alphabet.size()]);
	}

	@Override
	protected AtomicEvent nextEvent(Random random)
	{
		return m_alphabet[random.nextInt(m_alphabet.length)];
	}

}
