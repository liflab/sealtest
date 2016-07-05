package ca.uqac.lif.ecp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * A t-way trace generator. This is an implementation of the <i>t</i>-sequence
 * algorithm in the book <i>Introduction to Combinatorial Testing</i>, 
 * Figure 10.1.
 * 
 * @author Sylvain
 *
 * @param <T> The type of the events in the traces that are generated
 */
public class TWayScoringTraceGenerator<T extends Event> extends TraceGenerator<T> 
{
	/**
	 * The strength of the test suite (i.e. the <i>t</i> in
	 * <i>t</i>-way)
	 */
	protected int m_strength;
	
	/**
	 * The alphabet used
	 */
	protected Alphabet<T> m_alphabet;
	
	/**
	 * The number of candidate tests to generate at each iteration
	 */
	protected int m_numCandidates = 100;
	
	/**
	 * The maximum number of iterations of the algorithm, in case it does not
	 * succeed at achieving total coverage
	 */
	protected int m_maxIterations = 10000;
	
	/**
	 * A random number generator used to generate the random events
	 */
	protected Random m_random;
	
	/**
	 * Creates a new trace generator
	 * @param alphabet The alphabet of events
	 * @param strength The strength of the test suite (i.e. the <i>t</i> in
	 *   <i>t</i>-way)
	 */
	public TWayScoringTraceGenerator(Alphabet<T> alphabet, int strength, Random random)
	{
		super();
		m_strength = strength;
		m_alphabet = alphabet;
		m_random = random;
	}

	@Override
	public TestSuite<T> generateTraces() 
	{
		TestSuite<T> set = new TestSuite<T>();
		Set<Vector<T>> combinations = getCombinations();
		Map<Vector<T>,Boolean> covered_combinations = new HashMap<Vector<T>,Boolean>();
		for (Vector<T> combin : combinations)
		{
			covered_combinations.put(combin, false);
		}
		int num_covered_combinations = 0;
		int total_combinations = combinations.size();
		for (int it_count = 0; num_covered_combinations < total_combinations && it_count < m_maxIterations; it_count++)
		{
			Trace<T> best_trace = null;
			Map<Vector<T>,Boolean> new_covered_combinations = null;
			int new_num_covered_combinations = 0, improvement = 0;
			for (int trace_count = 0; trace_count < m_numCandidates; trace_count++)
			{
				Trace<T> trace = getRandomTrace();
				new_covered_combinations = new HashMap<Vector<T>,Boolean>();
				new_covered_combinations.putAll(covered_combinations);
				Set<Vector<T>> combin_in_trace = getCombinations(trace);
				for (Vector<T> combin : combin_in_trace)
				{
					new_covered_combinations.put(combin, true);
				}
				new_num_covered_combinations = countCoveredCombinations(new_covered_combinations);
				int new_improvement = new_num_covered_combinations - num_covered_combinations;
				if (new_improvement > improvement || best_trace == null)
				{
					// This trace adds more new combinations than the current best
					improvement = new_improvement;
					best_trace = trace;
				}
			}
			set.add(best_trace);
			covered_combinations = new_covered_combinations;
			num_covered_combinations += improvement;
		}
		return set;
	}
	
	/**
	 * Computes the number of covered combinations in the present map
	 * (i.e. the number of entries set to <code>true</code>)
	 * @param combinations The map of combinations
	 * @return The number of covered combinations
	 */
	protected int countCoveredCombinations(Map<Vector<T>,Boolean> combinations)
	{
		int count = 0;
		for (Vector<T> combin : combinations.keySet())
		{
			if (combinations.get(combin) == true)
			{
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Generates a random trace composed of all elements of the alphabet
	 * @return The trace
	 */
	protected Trace<T> getRandomTrace()
	{
		Trace<T> trace = new Trace<T>();
		List<T> symbols = new LinkedList<T>();
		symbols.addAll(m_alphabet);
		while (!symbols.isEmpty())
		{
			int pos = m_random.nextInt(symbols.size());
			trace.add(symbols.remove(pos));
		}
		return trace;
	}

	/**
	 * Gets the set of all combinations of t events
	 * @return The set of combinations
	 */
	protected Set<Vector<T>> getCombinations()
	{
		return getCombinations(new Vector<T>(), m_alphabet, m_strength);
	}
	
	/**
	 * Gets the set of all combinations of t events
	 * @param prefix
	 * @param remaining
	 * @param n
	 * @return The set of combinations
	 */
	protected Set<Vector<T>> getCombinations(Vector<T> prefix, Alphabet<T> remaining, int n)
	{
		Set<Vector<T>> set = new HashSet<Vector<T>>();
		if (n == 0)
		{
			set.add(prefix);
			return set;
		}
		for (T e : remaining)
		{
			Vector<T> new_v = new Vector<T>();
			new_v.addAll(prefix);
			new_v.add(e);
			Alphabet<T> new_alph = new Alphabet<T>(remaining);
			new_alph.remove(e);
			set.addAll(getCombinations(new_v, new_alph, n-1));
		}
		return set;
	}

	/**
	 * Gets the combinations of events contained in the current trace
	 * @param trace The trace
	 * @return The set of combinations
	 */
	protected Set<Vector<T>> getCombinations(Trace<T> trace)
	{
		return getCombinations(trace, new Vector<T>(), 0, m_strength);
	}
	
	/**
	 * Gets the combinations of events contained in the current trace
	 * @param trace The trace
	 * @param current The current prefix to be considered
	 * @param n The number of events remaining to put
	 * @return The set of combinations
	 */
	protected Set<Vector<T>> getCombinations(Trace<T> trace, Vector<T> current, int j, int n)
	{
		Set<Vector<T>> set = new HashSet<Vector<T>>();
		if (n == 0)
		{
			set.add(current);
			return set;
		}
		for (int i = j; i < trace.size(); i++)
		{
			Vector<T> new_current = new Vector<T>();
			new_current.addAll(current);
			new_current.add(trace.get(i));
			set.addAll(getCombinations(trace, new_current, i + 1, n-1));
		}
		return set;
	}

}
