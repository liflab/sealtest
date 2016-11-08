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

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * From a Cayley graph built from a classification function &kappa;,
 * creates a new Cayley graph representing the category prefix closure
 * of &kappa;. 
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
	public CayleyGraph<T,U> getClosureGraph(CayleyGraph<T,U> graph, int distance)
	{
		int start_id = graph.getInitialVertex().getId();
		MathSet<U> start_labelling = graph.getLabelling().get(start_id);
		StateVertex<T> start_vertex = new StateVertex<T>(start_id);
		Set<StateVertex<T>> new_vertices = new HashSet<StateVertex<T>>();
		new_vertices.add(start_vertex);
		CayleyVertexLabelling<U> labelling = new CayleyVertexLabelling<U>();
		labelling.put(start_vertex.getId(), start_labelling);
		traversalStep(graph, graph.getInitialVertex().getId(), distance, start_labelling, new_vertices, labelling);
		CayleyGraph<T,U> new_graph = new CayleyGraph<T,U>();
		for (StateVertex<T> vertex : new_vertices)
		{
			new_graph.add(vertex);
		}
		new_graph.setLabelling(labelling);
		return new_graph;
	}
	
	void traversalStep(CayleyGraph<T,U> in_graph, int id_source, int distance, MathSet<U> collated_labels, Set<StateVertex<T>> new_vertices, CayleyVertexLabelling<U> labelling)
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
			if (out_graph_dest == null)
			{
				out_graph_dest = new StateVertex<T>(in_graph_dest_id);
				new_vertices.add(out_graph_dest);
			}
			int out_graph_dest_id = out_graph_dest.getId();
			Edge<T> new_edge = new Edge<T>(out_graph_source_id, edge.getLabel(), out_graph_dest_id);
			out_graph_source.add(new_edge);
			labelling.put(out_graph_dest_id, new_labels);
			if (distance > 0)
			{
				traversalStep(in_graph, in_graph_dest_id, distance - 1, new_labels, new_vertices, labelling);
			}
		}
	}
	
	/**
	 * Gets a vertex based on its labelling
	 * @return
	 */
	protected StateVertex<T> getVertex(Set<StateVertex<T>> vertices, CayleyVertexLabelling<U> labelling, MathSet<U> labels, int id)
	{
		for (StateVertex<T> vertex : vertices)
		{
			if (vertex.m_state == id)
			{
				MathSet<U> vertex_labelling = labelling.get(vertex.getId());
				if (vertex_labelling.equals(labels))
				{
					return vertex;
				}
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
		
		public StateVertex(int state)
		{
			super();
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
