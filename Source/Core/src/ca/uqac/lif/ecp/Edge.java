package ca.uqac.lif.ecp;

/**
 * Edge of a Cayley graph
 * @author Sylvain
 *
 * @param <T> The type of the events. Edges of the graph are labelled
 *   with events 
 */
public class Edge<T extends Event> 
{
	/**
	 * The ID of the vertex in the graph that is the destination of this edge
	 */
	protected int m_source;
	
	/**
	 * The label attached to this edge
	 */
	protected T m_label;
	
	/**
	 * The ID of the vertex in the graph that is the destination of this edge
	 */
	protected int m_destination;
	
	/**
	 * Creates an edge
	 * @param label The label attached to this edge
	 * @param destination The ID of the vertex in the graph that is the 
	 *   destination of this edge
	 */
	public Edge(int source, T label, int destination)
	{
		super();
		m_label = label;
		m_destination = destination;
		m_source = source;
	}
	
	@Override
	public String toString()
	{
		return m_source + "-" + m_label + "->" + m_destination;
	}
	
	@Override
	public int hashCode()
	{
		return m_source + m_label.hashCode() + m_destination;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Edge))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		Edge<T> e = (Edge<T>) o;
		return m_source == e.m_source && m_label.equals(e.m_label) && m_destination == e.m_destination;
	}
}
