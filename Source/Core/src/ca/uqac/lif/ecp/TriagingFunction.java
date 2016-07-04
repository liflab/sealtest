package ca.uqac.lif.ecp;

public abstract class TriagingFunction<T,U> 
{
	/**
	 * Gets the equivalence class associated with a given trace
	 * @param The trace to read
	 * @return The equivalence class
	 */
	public U getClass(Trace<T> trace)
	{
		reset();
		U last = getStartClass();
		for (T event : trace)
		{
			last = read(event);
		}
		return last;
	}
	
	/**
	 * Resets the triaging function to its initial state
	 */
	public void reset()
	{
		// Do nothing
	}
	
	/**
	 * Reads the next event of the trace
	 * @param event The event
	 * @return The equivalence class that the trace read since
	 *   the last call to {@link #reset()} belongs to
	 */
	public abstract U read(T event);
	
	/**
	 * Gets the equivalence class for the empty trace
	 * @return The class
	 */
	public abstract U getStartClass();
}
