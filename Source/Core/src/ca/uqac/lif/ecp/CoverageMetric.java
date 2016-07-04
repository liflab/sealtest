package ca.uqac.lif.ecp;

import java.util.Set;

public abstract class CoverageMetric<T,V> 
{
	/**
	 * Creates a new coverage metric instance
	 * @param traces The set of traces we wish to measure coverage for
	 */
	public CoverageMetric()
	{
		super();
	}
	
	/**
	 * Gets the coverage of the set of traces according to the metric
	 * @param traces The set of traces we wish to measure coverage for
	 * @return The coverage
	 */
	public abstract V getCoverage(Set<Trace<T>> traces);
}
