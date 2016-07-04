package ca.uqac.lif.ecp;

import java.util.LinkedList;
import java.util.List;

/**
 * Vertex of a Cayley Graph
 * 
 * @param <T> The type of event this graph has for edge labels
 * @author Sylvain
 *
 */
public class Vertex<T extends Event>
{
	/**
	 * Counter for vertex IDs
	 */
	protected static int s_idCount = 0;
	
	/**
	 * The ID of this vertex
	 */
	protected int m_id;
	
	/**
	 * The set of edges in this graph
	 */
	protected List<Edge<T>> m_outEdges;
	
	/**
	 * Creates an empty vertex
	 */
	public Vertex()
	{
		super();
		m_id = s_idCount++;
		m_outEdges = new LinkedList<Edge<T>>();
	}
	
	/**
	 * Creates an empty vertex with given ID
	 */
	public Vertex(int id)
	{
		super();
		m_id = id;
		m_outEdges = new LinkedList<Edge<T>>();
	}
	
	/**
	 * Gets this vertex's ID
	 * @return The ID
	 */
	public int getId()
	{
		return m_id;
	}
	
	/**
	 * Adds an outgoing edge to this vertex
	 * @param e The Edge (must play in U2)
	 */
	public void add(Edge<T> e)
	{
		m_outEdges.add(e);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_id).append("\n");
		for (Edge<T> e : m_outEdges)
		{
			out.append("  ").append(e).append("\n");
		}
		return out.toString();
	}
	
	@Override
	public int hashCode()
	{
		return m_id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Vertex))
		{
			return false;
		}
		return m_id == ((Vertex<?>) o).m_id;
	}
	
	/**
	 * Gets the outgoing edges from this vertex
	 * @return The list of edges
	 */
	public List<Edge<T>> getEdges()
	{
		return m_outEdges;
	}
}
