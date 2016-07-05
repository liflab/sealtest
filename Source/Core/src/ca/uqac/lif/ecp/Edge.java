/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	protected final int m_source;
	
	/**
	 * The label attached to this edge
	 */
	protected final T m_label;
	
	/**
	 * The ID of the vertex in the graph that is the destination of this edge
	 */
	protected final int m_destination;
	
	/**
	 * A weight associated to the edge. Currently unused, but required for
	 * the minimum spanning tree algorithm we use 
	 */
	protected float m_weight = 1;
	
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
	
	/**
	 * Gets the label of the edge
	 * @return The label
	 */
	public T getLabel()
	{
		return m_label;
	}
	
	/**
	 * Gets the ID of the destination vertex for that edge
	 * @return The ID
	 */
	public int getDestination()
	{
		return m_destination;
	}
	
	/**
	 * Gets the ID of the source vertex for that edge
	 * @return The ID
	 */
	public int getSource()
	{
		return m_source;
	}
	
	/**
	 * Gets the edge's weight
	 * @return The weight
	 */
	public float getWeight()
	{
		return m_weight;
	}
	
	/**
	 * Sets the edge's weight
	 * @param weight The weight
	 */
	public void setWeight(float weight)
	{
		m_weight = weight;
	}
}
