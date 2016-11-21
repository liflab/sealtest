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
package ca.uqac.lif.ecp.graphs;

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;

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
	 * The ID of this vertex
	 */
	protected int m_id;
	
	/**
	 * The set of edges in this graph
	 */
	protected Set<Edge<T>> m_outEdges;
	
	/**
	 * Creates a copy of a vertex
	 * @param v The vertex to copy
	 */
	public Vertex(Vertex<T> v)
	{
		super();
		m_id = v.m_id;
		m_outEdges = new HashSet<Edge<T>>();
		// Edges are immutable, so no need to duplicate them
		m_outEdges.addAll(v.m_outEdges);
	}
	
	/**
	 * Creates an empty vertex with given ID
	 */
	public Vertex(int id)
	{
		super();
		m_id = id;
		m_outEdges = new HashSet<Edge<T>>();
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
	public Set<Edge<T>> getEdges()
	{
		return m_outEdges;
	}
	
	/**
	 * Determines if a vertex is a left, i.e. has no outgoing edges
	 * @return True if the vertex is a leaf, false otherwise
	 */
	public boolean isLeaf()
	{
		return m_outEdges.isEmpty();
	}
}
