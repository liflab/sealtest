package ca.uqac.lif.ecp;

import java.util.Set;

public abstract class CoverageMetric<T,V> 
{
	/**
	 * The set of traces we wish to measure coverage for
	 */
	protected Set<Trace<T>> m_traces;
	
	/**
	 * Creates a new coverage metric instance
	 * @param traces The set of traces we wish to measure coverage for
	 */
	public CoverageMetric(Set<Trace<T>> traces)
	{
		super();
		m_traces = traces;
	}
	
	/**
	 * Gets the coverage of the set of traces according to the metric
	 * @return The coverage
	 */
	public abstract V getCoverage();
}
