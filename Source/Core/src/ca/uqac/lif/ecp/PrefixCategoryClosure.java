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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * From a Cayley graph built from a classification function &kappa;,
 * creates a new Cayley graph representing the prefix closure
 * of &int;&kappa;.
 * <p>
 * <strong>Caveat emptor:</strong> the original graph must be complete,
 * i.e. for an alphabet &Sigma;, from any vertex, there must be an outgoing
 * transition with symbol &sigma; for every &sigma;&nbsp;&in;&nbsp;&Sigma;.
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class PrefixCategoryClosure<T extends Event,U> 
{
	/**
	 * The maximum recursion depth
	 */
	protected static final int s_maxDepth = 20;
	
	protected static int s_idCounter = 0;

	/**
	 * Gets the prefix closure graph of the Cayley graph
	 * @param graph The graph
	 * @return The prefix closure graph
	 */
	public CayleyGraph<T,U> getClosureGraph(CayleyGraph<T,U> graph)
	{
		int start_id = graph.getInitialVertex().getId();
		MathSet<U> start_labelling = graph.getLabelling().get(start_id);
		s_idCounter = 0;
		StateVertex<T> start_vertex = new StateVertex<T>(s_idCounter++, start_id);
		//Set<StateVertex<T>> new_vertices = new HashSet<StateVertex<T>>();
		Map<Integer,Set<StateVertex<T>>> new_vertices = new HashMap<Integer,Set<StateVertex<T>>>();
		Set<StateVertex<T>> sv = new HashSet<StateVertex<T>>();
		sv.add(start_vertex);
		new_vertices.put(start_vertex.m_state, sv);
		CayleyVertexLabelling<U> labelling = new CayleyVertexLabelling<U>();
		labelling.put(start_vertex.getId(), start_labelling);
		traversalStep(graph, graph.getInitialVertex().getId(), s_maxDepth, start_labelling, new_vertices, labelling);
		CayleyGraph<T,U> new_graph = new CayleyGraph<T,U>();
		for (Set<StateVertex<T>> vertex_set : new_vertices.values())
		{
			for (StateVertex<T> vertex : vertex_set)
			{
				new_graph.add(vertex);
			}
		}
		new_graph.setInitialVertexId(start_vertex.getId());
		new_graph.setLabelling(labelling);
		return new_graph;
	}

	void traversalStep(CayleyGraph<T,U> in_graph, int id_source, int distance, MathSet<U> collated_labels, Map<Integer,Set<StateVertex<T>>> new_vertices, CayleyVertexLabelling<U> labelling)
	{
		Vertex<T> in_graph_source = in_graph.getVertex(id_source);
		StateVertex<T> out_graph_source = getVertex(new_vertices, labelling, collated_labels, id_source);
		assert out_graph_source != null;
		int out_graph_source_id = out_graph_source.getId();
		// Get outgoing edges
		for (Edge<T> edge : in_graph_source.getEdges())
		{
			int in_graph_dest_id = edge.getDestination();
			MathSet<U> new_labels = new MathSet<U>(collated_labels);
			MathSet<U> dest_labelling = in_graph.getLabelling().get(in_graph_dest_id);
			new_labels.addAll(dest_labelling);
			// Does this vertex already exist?
			StateVertex<T> out_graph_dest = getVertex(new_vertices, labelling, new_labels, in_graph_dest_id);
			boolean new_target = false;
			if (out_graph_dest == null)
			{
				out_graph_dest = new StateVertex<T>(s_idCounter++, in_graph_dest_id);
				Set<StateVertex<T>> sv;
				if (!new_vertices.containsKey(out_graph_dest.m_state))
				{
					sv = new HashSet<StateVertex<T>>();
				}
				else
				{
					sv = new_vertices.get(out_graph_dest.m_state);
				}
				sv.add(out_graph_dest);
				new_vertices.put(out_graph_dest.m_state, sv);
				int total_size = totalSize(new_vertices);
				if (total_size % 1000 == 0)
				{
					System.err.print(total_size + " ");
				}
				new_target = true;
			}
			int out_graph_dest_id = out_graph_dest.getId();
			Edge<T> new_edge = new Edge<T>(out_graph_source_id, edge.getLabel(), out_graph_dest_id);
			out_graph_source.add(new_edge);
			labelling.put(out_graph_dest_id, new_labels);
			if (new_target && distance > 0)
			{
				traversalStep(in_graph, in_graph_dest_id, distance - 1, new_labels, new_vertices, labelling);
			}
		}
	}
	
	protected int totalSize(Map<Integer,Set<StateVertex<T>>> new_vertices)
	{
		int x = 0;
		for (Set<StateVertex<T>> vset : new_vertices.values())
		{
			x += vset.size();
		}
		return x;
	}

	/**
	 * Gets a vertex based on its labelling
	 * @return
	 */
	protected StateVertex<T> getVertex(Map<Integer,Set<StateVertex<T>>> vertices, CayleyVertexLabelling<U> labelling, MathSet<U> labels, int id)
	{
		if (!vertices.containsKey(id))
		{
			return null;
		}
		Set<StateVertex<T>> vertex_set = vertices.get(id);
		for (StateVertex<T> vertex : vertex_set)
		{
			MathSet<U> vertex_labelling = labelling.get(vertex.getId());
			if (vertex_labelling.equals(labels))
			{
				return vertex;
			}
		}
		return null;
	}

	/**
	 * Special type of vertex that includes a state ID in addition to the
	 * information carried by a normal vertex
	 */
	class StateVertex<X extends Event> extends Vertex<X>
	{
		int m_state;

		public StateVertex(int id, int state)
		{
			super(id);
			m_state = state;
		}

		public StateVertex(int state, Vertex<X> vertex)
		{
			super(vertex);
			m_state = state;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode() + m_state;
		}

		@SuppressWarnings("unchecked")
		public boolean equals(Object o)
		{
			return o != null && o instanceof StateVertex 
					&& ((StateVertex<X>) o).m_state == m_state && super.equals((Vertex<X>) o);
		}
	}
}
