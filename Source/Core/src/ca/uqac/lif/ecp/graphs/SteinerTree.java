package ca.uqac.lif.ecp.graphs;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathSet;

/**
 * Solves the directed Steiner tree problem.
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events that are the labels of the edges
 * @param <U> The type of categories of the underlying triaging function
 */
public abstract class SteinerTree<T extends Event, U> extends CayleyGraphSolver<T, U> 
{
	/**
	 * The set of vertices that must be included in the tree
	 */
	protected MathSet<Integer> m_importantVertices;
	
	/**
	 * Creates a new solver
	 * @param graph The graph this solver will work on
	 */
	public SteinerTree(CayleyGraph<T, U> graph) 
	{
		super(graph);
	}

	/**
	 * Creates a new solver
	 * @param graph The graph this solver will work on
	 * @param vertices The set of vertices that must be included in the tree
	 */
	public SteinerTree(CayleyGraph<T, U> graph, MathSet<Integer> vertices) 
	{
		super(graph);
		m_importantVertices = vertices;
	}
	
	/**
	 * Gets a Steiner tree from the Cayley graph and set of important
	 * vertices
	 * @return The tree. It is assumed that the graph from which the tree
	 *   is constructed has a single initial node; therefore this node is
	 *   also the root of the returned tree.
	 */
	public abstract CayleyGraph<T,U> getTree();

}
