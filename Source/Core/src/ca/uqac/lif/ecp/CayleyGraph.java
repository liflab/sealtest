package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	 * Produces a Dot file to draw the graph
	 * @return A string that can be passed to Dot
	 */
	public String toDot()
	{
		StringBuilder out = new StringBuilder();
		out.append("digraph G {\n");
		for (Vertex<T> v : m_vertices)
		{
			int id = v.getId();
			out.append(" ").append(id).append("[label=\"").append(m_labelling.get(id)).append("\"];\n");
			printEdges(v.m_outEdges, id, out);
		}
		// Add initial state
		out.append("i -> 0;\n");
		out.append("i [shape=\"none\",label=\"\"];\n");
		out.append("}");
		return out.toString();
	}
	
	/**
	 * Prints the edges of the graph, by merging the label of all edges
	 * with the same target state
	 * @param edges The list of edges
	 * @param source_id The ID of the source state for all the labels
	 * @param out The StringBuilder to write to
	 */
	protected void printEdges(List<Edge<T>> edges, int source_id, StringBuilder out)
	{
		Map<Integer,String> edge_labels = new HashMap<Integer,String>();
		for (Edge<T> e : edges)
		{
			String edge_label = "";
			if (edge_labels.containsKey(e.m_destination))
			{
				edge_label = edge_labels.get(e.m_destination);
			}
			edge_label += e.m_label + ",";
			edge_labels.put(e.m_destination, edge_label);
		}
		for (int dest : edge_labels.keySet())
		{
			String label = edge_labels.get(dest);
			label = label.substring(0, label.length() - 1);
			out.append(" ").append(source_id).append(" -> ").append(dest);
			out.append("[label=\"").append(label).append("\"];\n");
		}
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
}
