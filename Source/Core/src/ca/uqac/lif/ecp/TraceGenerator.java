package ca.uqac.lif.ecp;

import java.util.Set;

/**
 * Generates a set of traces
 * @param <T> The type of the events in the traces that are generated
 */
public abstract class TraceGenerator<T> 
{
	/**
	 * Generates a set of traces
	 * @return The set of traces
	 */
	public abstract Set<Trace<T>> generateTraces();
}
