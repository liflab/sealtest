package ca.uqac.lif.ecp;

import java.util.Random;

/**
 * Trace generator that picks events randomly
 * 
 * @param <T> The type of the events in the traces that are generated
 */
public abstract class RandomTraceGenerator<T> extends TraceGenerator<T>
{
	/**
	 * A random number generator used to generate the random events
	 */
	protected Random m_random;

	/**
	 * Generates a new event randomly
	 * @param random The random number generator used to generate the event
	 * @return A new event
	 */
	protected abstract T nextEvent(Random random);
	
	public RandomTraceGenerator(Random random)
	{
		super();
		m_random = random;
	}
}
