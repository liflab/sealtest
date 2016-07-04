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
	public CayleyEquivalenceCoverage(CayleyGraph<T, U> graph, TriagingFunction<T,U> function) 
	{
		super(graph, function);
	}

	@Override
	public Float getCoverage(Set<Trace<T>> traces) 
	{
		float nb_total_classes = m_graph.getLabelling().values().size();
		if (nb_total_classes == 0)
		{
			return 0f;
		}
		Set<U> covered_classes = new HashSet<U>();
		for (Trace<T> trace : traces)
		{
			m_function.reset();
			for (T event : trace)
			{
				U category = m_function.read(event);
				covered_classes.add(category);
			}
		}
		return (float) covered_classes.size() / nb_total_classes;
	}

}
