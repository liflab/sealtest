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

import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * From a Cayley graph built from a classification function &kappa;,
 * creates a new Cayley graph representing the category prefix closure
 * of &kappa;. 
 * 
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class PrefixCategoryClosure<T extends Event,U> 
{
	public CayleyGraph<T,U> getClosureGraph(CayleyGraph<T,U> graph, int distance)
	{
		CayleyGraph<T,U> new_graph = new CayleyGraph<T,U>();
		int start_id = graph.getInitialVertex().getId();
		MathSet<U> start_labelling = graph.getLabelling().get(start_id);
		Vertex<T> start_vertex = new Vertex<T>();
		new_graph.add(start_vertex);
		new_graph.getLabelling().put(start_vertex.getId(), start_labelling);
		traversalStep(graph, graph.getInitialVertex().getId(), distance, start_labelling, new_graph);
		return new_graph;
	}
	
	void traversalStep(CayleyGraph<T,U> in_graph, int id_source, int distance, MathSet<U> collated_labels, CayleyGraph<T,U> out_graph)
	{
		Vertex<T> in_graph_source = in_graph.getVertex(id_source);
		//int in_graph_source_id = id_source;
		Vertex<T> out_graph_source = out_graph.getFirstVertexWithLabelling(collated_labels);
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
			Vertex<T> out_graph_dest = out_graph.getFirstVertexWithLabelling(new_labels);
			if (out_graph_dest == null)
			{
				out_graph_dest = new Vertex<T>();
				out_graph.add(out_graph_dest);
			}
			int out_graph_dest_id = out_graph_dest.getId();
			Edge<T> new_edge = new Edge<T>(out_graph_source_id, edge.getLabel(), out_graph_dest_id);
			out_graph_source.add(new_edge);
			out_graph.getLabelling().put(out_graph_dest_id, new_labels);
			if (distance > 0)
			{
				traversalStep(in_graph, in_graph_dest_id, distance - 1, new_labels, out_graph);
			}
		}
	}
}
