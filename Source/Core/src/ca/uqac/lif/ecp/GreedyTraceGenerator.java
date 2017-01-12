package ca.uqac.lif.ecp;

import java.util.Random;

/**
 * Generates a test suite using a greedy algorithm. The algorithm works as
 * follows; given integers M and N:
 * <ol>
 * <li>Generate N traces, each having a length randomly picked between 1
 * and M</li>
 * <li>Pick the trace that maximizes the improvement on coverage according to
 * the specified metric</li>
 * <li>Repeat until compete coverage is reached and output the resulting
 * test suite</li>
 * </ol>
 * In case complete coverage is very hard to achieve using this algorithm,
 * a maximum number of iterations is also programmed (and usually set to a
 * high value), in order to prevent quasi-infinite loops. When this maximum
 * number of iterations is reached, the test suite is output even if
 * complete coverage is not obtained.
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The type of event in the generated traces
 */
public abstract class GreedyTraceGenerator<T extends Event> extends TraceGenerator<T>
{
	/**
	 * The random number generator used to generate the traces
	 */
	protected final Random m_random;
	
	/**
	 * The coverage metric used to evaluate the generated traces
	 */
	protected final CoverageMetric<T,Float> m_metric;
	
	/**
	 * The number of candidate traces to generate at each pass
	 */
	protected int m_numCandidates = 20;
	
	/**
	 * The maximum number of iterations of the algorithm before giving up
	 */
	protected int m_maxIterations = 10;
	
	/**
	 * The coverage obtained the last time {@link #generateTraces()} was
	 * called
	 */
	protected float m_lastCoverage = -1;
	
	public GreedyTraceGenerator(Random random, CoverageMetric<T,Float> metric)
	{
		super();
		m_random = random;
		m_metric = metric;
	}
	
	/**
	 * Sets the maximum number of iterations of the algorithm before giving up
	 * @param iterations The number of iterations
	 */
	public void setMaxIterations(int iterations)
	{
		m_maxIterations = iterations;
	}
	
	/**
	 * Gets the coverage obtained the last time {@link #generateTraces()} was
	 * called
	 * @return The coverage, -1 if the method has not been called yet
	 */
	public float getLastCoverage()
	{
		return m_lastCoverage;
	}

	@Override
	public TestSuite<T> generateTraces() 
	{
		TestSuite<T> generated_suite = new TestSuite<T>();
		float current_coverage = 0;
		for (int it_cnt = 0; it_cnt < m_maxIterations; it_cnt++)
		{
			float new_coverage = current_coverage;
			Trace<T> candidate_trace = null;
			for (int cnd_cnt = 0; cnd_cnt < m_numCandidates; cnd_cnt++)
			{
				int length = pickLength();
				Trace<T> trace = generateTrace(length);
				if (trace != null)
				{
					TestSuite<T> new_suite = new TestSuite<T>(generated_suite);
					new_suite.add(trace);
					float coverage = m_metric.getCoverage(new_suite);
					if (coverage > new_coverage)
					{
						// This is a new best
						candidate_trace = trace;
						new_coverage = coverage;
					}
				}
			}
			if (candidate_trace != null)
			{
				generated_suite.add(candidate_trace);
				current_coverage = new_coverage;
			}
			m_lastCoverage = current_coverage;
			if (current_coverage >= 1)
			{
				break;
			}
		}
		m_lastCoverage = Math.min(1, m_lastCoverage);
		return generated_suite;
	}
	
	/**
	 * Chooses a length for a trace to be generated 
	 * @return The length
	 */
	public int pickLength()
	{
		return m_random.nextInt(100);
	}
	
	/**
	 * Randomly generates a trace of given length
	 * @param length The length
	 * @return The trace, or {@code null} if no trace could be generated
	 */
	public abstract Trace<T> generateTrace(int length);
}
