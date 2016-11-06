package ca.uqac.lif.ecp;

/**
 * Trace generator using a Cayley Graph.
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public abstract class CayleyGraphTraceGenerator<T extends Event,U extends Object> extends TraceGenerator<T>
{
	/**
	 * The Cayley graph used to generate the traces
	 */
	protected final CayleyGraph<T,U> m_graph;
	
	public CayleyGraphTraceGenerator(CayleyGraph<T,U> graph)
	{
		super();
		m_graph = graph;
	}
}
