package ca.uqac.lif.ecp;

import java.util.HashSet;
import java.util.Set;

/**
 * Coverage of a set of traces based on the fraction of all equivalence classes
 * of the triaging function that are present in one of the prefixes of some
 * trace.
 * @author Sylvain
 *
 * @param <T> The type of events in the trace
 * @param <U> The output type of the triaging function
 */
public class CayleyEquivalenceCoverage<T extends Event,U extends Object> extends CayleyCoverageMetric<T,U,Float> 
{
	public CayleyEquivalenceCoverage(CayleyGraph<T, U> graph, TriagingFunction<T,U> function, Set<Trace<T>> traces) 
	{
		super(graph, function, traces);
	}

	@Override
	public Float getCoverage() 
	{
		float nb_total_classes = m_graph.getLabelling().values().size();
		Set<U> covered_classes = new HashSet<U>();
		for (Trace<T> trace : m_traces)
		{
			for (T event : trace)
			{
				U category = m_function.read(event);
				covered_classes.add(category);
			}
		}
		return (float) covered_classes.size() / nb_total_classes;
	}

}
