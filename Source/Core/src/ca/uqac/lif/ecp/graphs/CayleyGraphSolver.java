package ca.uqac.lif.ecp.graphs;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;

/**
 * Solves a problem on a generalized Cayley graph
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public abstract class CayleyGraphSolver<T extends Event,U>
{
	/**
	 * The graph this solver will work on
	 */
	protected CayleyGraph<T,U> m_graph;
	
	/**
	 * Instantiates a new solver
	 * @param graph The graph this solver will work on
	 */
	public CayleyGraphSolver(CayleyGraph<T,U> graph)
	{
		super();
		m_graph = graph;
	}
	
	/**
	 * Sets the graph this solver will work on
	 * @param graph The graph
	 */
	public void setGraph(CayleyGraph<T,U> graph)
	{
		m_graph = graph;
	}

}
