package ca.uqac.lif.ecp.atomic;

import java.util.Random;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CoverageMetric;
import ca.uqac.lif.ecp.ThresholdRandomTraceGenerator;

public class AtomicThresholdRandomGenerator extends ThresholdRandomTraceGenerator<AtomicEvent>
{
	/**
	 * The alphabet to pick events from
	 */
	protected AtomicEvent[] m_alphabet;
	
	public AtomicThresholdRandomGenerator(Random random, CoverageMetric<AtomicEvent,Float> metric, int max_trace_length, float threshold, Alphabet<AtomicEvent> alphabet) 
	{
		super(random, metric, max_trace_length, threshold);
		m_alphabet = alphabet.toArray(new AtomicEvent[alphabet.size()]);
	}

	@Override
	protected AtomicEvent nextEvent(Random random)
	{
		return m_alphabet[random.nextInt(m_alphabet.length)];
	}
}
