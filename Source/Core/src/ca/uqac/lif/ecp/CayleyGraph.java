package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of a Cayley Graph.
 * (TODO: describe what a Cayley Graph is)
 * 
 * @param <T> The type of event this graph has for edge labels
 * @author Sylvain
 *
 */
public class CayleyGraph<T extends Event>
{
	/**
	 * The set of vertices of this graph. Each vertex is responsible
	 * for storing its outgoing edges.
	 */
	public Set<Vertex<T>> m_vertices;
	
	/**
	 * Creates an empty graph
	 */
	public CayleyGraph()
	{
		super();
		m_vertices = new HashSet<Vertex<T>>();
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
	public CayleyGraph<T> add(Vertex<T> v)
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
				int j = labels.indexOf(e.m_destination);
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
}
