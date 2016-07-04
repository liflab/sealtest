package ca.uqac.lif.ecp;

import java.util.Set;

public abstract class CayleyCoverageMetric<T extends Event,U extends Object,V> extends CoverageMetric<T,V> 
{
	/**
	 * The Cayley Graph from which the coverage will be computed
	 */
	protected CayleyGraph<T,U> m_graph;
	
	/**
	 * The triaging function used to classify traces
	 */
	protected TriagingFunction<T,U> m_function;
	
	public CayleyCoverageMetric(CayleyGraph<T,U> graph, TriagingFunction<T,U> function, Set<Trace<T>> traces)
	{
		super(traces);
		m_graph = graph;
		m_function = function;
	}
}
