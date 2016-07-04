package ca.uqac.lif.ecp;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Trace generator that picks events randomly. The trace generator is
 * instructed to generate <i>n</i> traces, each of length <i>m</i>, by
 * picking at random each event in the set of possible events.
 */
public abstract class ThresholdRandomTraceGenerator<T> extends RandomTraceGenerator<T>
{
	/**
	 * The coverage metric used to compute the coverage of the generated
	 * traces
	 */
	CoverageMetric<T,Float> m_metric;
	
	/**
	 * The maximum length of the traces to generate
	 */
	protected int m_maxTraceLength;
	
	/**
	 * The coverage threshold to obtain with the set of traces
	 */
	protected float m_coverageThreshold = 0;
	
	/**
	 * The maximum number of traces to generate
	 */
	protected int m_maxTraces = 100000;

	public ThresholdRandomTraceGenerator(Random random, CoverageMetric<T,Float> metric, int max_trace_length, float threshold)
	{
		super(random);
		m_metric = metric;
		m_maxTraceLength = max_trace_length;
		m_coverageThreshold = threshold;
	}

	@Override
	public Set<Trace<T>> generateTraces() 
	{
		float coverage = 0;
		Set<Trace<T>> out_set = new HashSet<Trace<T>>();
		for (int it_count = 0; coverage < m_coverageThreshold && it_count < m_maxTraces; it_count++)
		{
			Trace<T> trace = new Trace<T>();
			int trace_length = m_random.nextInt(m_maxTraceLength);
			for (int len_count = 0; len_count < trace_length; len_count++)
			{
				T event = nextEvent(m_random);
				trace.add(event);
			}
			out_set.add(trace);
			coverage = m_metric.getCoverage(out_set);
		}
		return out_set;
	}

}
