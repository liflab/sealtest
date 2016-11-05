/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hall�

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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.ecp.graphs.LabelledGraph;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;
import ca.uqac.lif.structures.Matrix;

/**
 * Implementation of a Cayley Graph. The vertices of the graph are 
 * identified by integers.
 * 
 * @param <T> The type of event this graph has for edge labels
 * @author Sylvain Hall�
 *
 */
public class CayleyGraph<T extends Event,U extends Object> extends LabelledGraph<T>
{
	
	/**
	 * The labelling associated to each vertex
	 */
	CayleyVertexLabelling<U> m_labelling;
	
	/**
	 * Creates an empty graph
	 */
	public CayleyGraph()
	{
		super();
		m_labelling = new CayleyVertexLabelling<U>();
	}
	
	/**
	 * Creates a copy of a Cayley graph
	 * @param graph The graph to copy
	 */
	public CayleyGraph(CayleyGraph<T,U> graph)
	{
		super(graph);
		m_labelling = graph.m_labelling;
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
		int size = getVertexCount();
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
			for (Edge<T> e : v.getEdges())
			{
				int j = labels.indexOf(e.getDestination());
				table[j][i]++;
			}
		}
		return table;
	}
	
	/**
	 * Gets the first vertex that is labelled with a given equivalence class
	 * @param eq_class The equivalence class
	 * @return The first vertex found, null if no vertex exists with that
	 *   category
	 */
	protected Vertex<T> getFirstVertexWithLabelling(MathSet<U> eq_class)
	{
		for (Vertex<T> v : getVertices())
		{
			if (m_labelling.get(v.getId()).equals(eq_class))
			{
				return v;
			}
		}
		return null;
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
	public void setLabelling(CayleyVertexLabelling<U> l)
	{
		m_labelling = l;
	}
	
	/**
	 * Gets the labelling associated to the vertices of the graph
	 * @return The labelling
	 */
	public CayleyVertexLabelling<U> getLabelling()
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
	public Map<MathSet<U>,Integer> getClassCardinality(int length, boolean cumulative)
	{
		Map<MathSet<U>,Integer> cardinalities = new HashMap<MathSet<U>,Integer>();
		for (MathSet<U> category : m_labelling.values())
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
			MathSet<U> category = m_labelling.get(vertex_id);
			cardinalities.put(category, cardinalities.get(category) + (int) V_cumul[i]);
		}
		return cardinalities;
	}	
}
