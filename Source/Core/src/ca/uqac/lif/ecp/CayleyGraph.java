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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a Cayley Graph.
 * (TODO: describe what a Cayley Graph is)
 * 
 * @param <T> The type of event this graph has for edge labels
 * @author Sylvain
 *
 */
public class CayleyGraph<T extends Event,U extends Object>
{
	/**
	 * The set of vertices of this graph. Each vertex is responsible
	 * for storing its outgoing edges.
	 */
	public Set<Vertex<T>> m_vertices;
	
	/**
	 * The labelling associated to each vertex
	 */
	VertexLabelling<U> m_labelling;
	
	/**
	 * Creates an empty graph
	 */
	public CayleyGraph()
	{
		super();
		m_vertices = new HashSet<Vertex<T>>();
		m_labelling = new VertexLabelling<U>();
	}
	
	/**
	 * Creates a copy of a Cayley graph
	 * @param graph The graph to copy
	 */
	public CayleyGraph(CayleyGraph<T,U> graph)
	{
		this();
		for (Vertex<T> v : graph.m_vertices)
		{
			m_vertices.add(new Vertex<T>(v));
		}
		m_labelling = graph.m_labelling;
	}
	
	/**
	 * Gets the vertex with given ID
	 * @param id The ID
	 * @return The vertex, or null if no vertex exists with that ID
	 */
	public Vertex<T> getVertex(int id)
	{
		for (Vertex<T> v : m_vertices)
		{
			if (v.m_id == id)
			{
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Adds a vertex to this graph
	 * @param v The vertex
	 * @return This graph
	 */
	public CayleyGraph<T,U> add(Vertex<T> v)
	{
		m_vertices.add(v);
		return this;
	}
	
	/**
	 * Produces the adjacency matrix of the Cayley Graph. This matrix is
	 * such that entry (<i>i</i>,<i>j</i>) = <i>k</i> if there exist <i>k</i> 
	 * directed edges from
	 * vertex labelled <i>i</i> to vertex labelled <i>j</i>. Although the
	 * entries of that matrix are natural numbers, their type is <tt>float</tt>,
	 * as that matrix can be used in algorithms that multiply it by a vector
	 * of <tt>float</tt>s.
	 * 
	 * @return The matrix
	 */
	public float[][] getAdjacencyMatrix()
	{
		int size = m_vertices.size();
		// Create empty table and fill with 0
		float table[][] = new float[size][size];
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				table[i][j] = 0;
			}
		}
		// Get vertex labels in a sorted list
		List<Integer> labels = getVertexLabels();
		Collections.sort(labels);
		// Iterate over each vertex...
		for (int i = 0; i < size; i++)
		{
			int label = labels.get(i);
			Vertex<T> v = getVertex(label);
			// Iterate over each edge leaving this vertex
			for (Edge<T> e : v.m_outEdges)
			{
				int j = labels.indexOf(e.getDestination());
				table[j][i]++;
			}
		}
		return table;
	}
	
	/**
	 * Fetches the list of all vertex IDs present in the graph 
	 * @return The list of IDs
	 */
	public List<Integer> getVertexLabels()
	{
		List<Integer> out = new ArrayList<Integer>();
		for (Vertex<T> v : m_vertices)
		{
			out.add(v.m_id);
		}
		return out;
	}
	
	/**
	 * Gets the first vertex that is labelled with a given category
	 * @param category The category
	 * @return The first vertex found, null if no vertex exists with that
	 *   category
	 */
	protected Vertex<T> getFirstVertexWithLabelling(U category)
	{
		for (Vertex<T> v : m_vertices)
		{
			if (m_labelling.get(v.getId()).equals(category))
			{
				return v;
			}
		}
		return null;
	}

	/**
	 * Gets the vertex with given ID
	 * @param id The ID
	 * @return The vertex, or null if not found
	 */
	public Vertex<T> getVertexWithId(int id)
	{
		for (Vertex<T> v : m_vertices)
		{
			if (v.getId() == id)
			{
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Gets the number of edges in the graph
	 * @return The number of edges
	 */
	public int getEdgeCount()
	{
		int count = 0;
		for (Vertex<T> v : m_vertices)
		{
			count += v.m_outEdges.size();
		}
		return count;
	}
	
	/**
	 * Gets the number of vertices in the graph
	 * @return The number of vertices
	 */
	public int getVertexCount()
	{
		return m_vertices.size();
	}
	
	/**
	 * Gets an instance of a plotter for this Cayley graph
	 * @return The plotter
	 */
	public GraphPlotter<T,U> plotter()
	{
		return new GraphPlotter<T,U>(this);
	}
	
	/**
	 * Sets the labelling associated to the vertices of the graph
	 * @param l The labelling
	 */
	public void setLabelling(VertexLabelling<U> l)
	{
		m_labelling = l;
	}
	
	/**
	 * Gets the labelling associated to the vertices of the graph
	 * @return The labelling
	 */
	public VertexLabelling<U> getLabelling()
	{
		return m_labelling;
	}
	
	/**
	 * Computes the number of traces of a given length that belong to
	 * each equivalence class
	 * @param length The length
	 * @param cumulative If set to true, will compute the number of traces
	 *   of length at most <tt>length</tt> (instead of <em>exactly</em> of length
	 *   <tt>length</tt>) 
	 * @return A map telling how many traces there are for each equivalence
	 *   class
	 */
	public Map<U,Integer> getClassCardinality(int length, boolean cumulative)
	{
		Map<U,Integer> cardinalities = new HashMap<U,Integer>();
		for (U category : m_labelling.values())
		{
			cardinalities.put(category, 0);
		}
		float[][] M = getAdjacencyMatrix();
		List<Integer> labels = getVertexLabels();
		Collections.sort(labels);
		// Create empty vector, put 1 as its first component
		float[] V = new float[M[0].length];
		float[] V_cumul = new float[M[0].length];
		for (int i = 0; i < V.length; i++)
		{
			V[i] = 0;
		}
		V[getInitialVertex().getId()] = 1;
		// Repeatedly multiply M by V (n times) 
		float[] V_prime = null;
		for (int it_count = 0; it_count < length; it_count++)
		{
			V_prime = Matrix.multiply(M, V);
			for (int i = 0; i < V.length; i++)
			{
				if (cumulative)
				{
					V_cumul[i] = V_prime[i];
				}
				else
				{
					V_cumul[i] += V_prime[i];
				}
			}
			V = V_prime;
		}
		// Fill map
		for (int i = 0; i < V.length; i++)
		{
			int vertex_id = labels.get(i);
			U category = m_labelling.get(vertex_id);
			cardinalities.put(category, cardinalities.get(category) + (int) V_cumul[i]);
		}
		return cardinalities;
	}
	
	/**
	 * Gets the set of vertices of this graph
	 * @return The set of vertices
	 */
	public Set<Vertex<T>> getVertices()
	{
		return m_vertices;
	}
	
	/**
	 * Gets the list of all edges in the graph
	 * @return The list of edges
	 */
	public List<Edge<T>> getEdges()
	{
		List<Edge<T>> edges = new LinkedList<Edge<T>>();
		for (Vertex<T> v : m_vertices)
		{
			edges.addAll(v.getEdges());
		}
		return edges;
	}

	/**
	 * Gets the initial vertex of the Cayley graph
	 * @return The initial vertex
	 */
	public Vertex<T> getInitialVertex()
	{
		// TODO: don't hardcode 0
		return getVertex(0);
	}
}
