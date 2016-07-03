package ca.uqac.lif.ecp;

public interface TriagingFunction<T,U> 
{
	/**
	 * Gets the equivalence class associated with a given trace
	 * @param The trace to read
	 * @return The equivalence class
	 */
	public U getClass(Trace<T> trace);
}
