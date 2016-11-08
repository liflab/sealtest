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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.graphs.Vertex;

/**
 * Generates a plot from a Cayley Graph
 *
 * @param <T> The type of the events in the graph
 * @param <U> The output type of the triaging function used to create
 *   the graph 
 */
public class GraphPlotter<T extends Event,U extends Object>
{
	/**
	 * The format of the resulting graph. This can either be:
	 * <ul>
	 * <li><code>DOT</code>: standard Dot file with plain text labels. 
	 * The resulting string can be saved to a file and the processed with
	 * <a href="http://graphviz.org">Graphviz</a></li>
	 * <li><code>LADOT</code>: extended Dot file with LaTeX labels. You need
	 * <a href="http://brighten.bigw.org/projects/ladot/">Ladot</a> to
	 * process the resulting string</li>
	 * </ul>
	 */
	public static enum Format {DOT, LADOT};

	/**
	 * The Cayley graph to plot
	 */
	protected final CayleyGraph<T,U> m_graph;
		
	/**
	 * Creates a new plotter for a given graph
	 * @param graph The graph
	 */
	public GraphPlotter(CayleyGraph<T,U> graph)
	{
		super();
		m_graph = graph;
	}
	
	/**
	 * Produces a Dot file to draw the graph
	 * @param format The format to print out
	 * @return A string that can be passed to Dot
	 */
	public String toDot(Format format)
	{
		StringBuilder out = new StringBuilder();
		out.append("digraph G {\n");
		for (Vertex<T> v : m_graph.getVertices())
		{
			int id = v.getId();
			out.append(" ").append(id).append("[label=\"").append(printVertex(v, format)).append("\"];\n");
			out.append(printEdges(v.getEdges(), id, format));
		}
		// Add initial state
		out.append("i -> 0;\n");
		out.append("i [shape=\"none\",label=\"\"];\n");
		out.append("}");
		return out.toString();
	}

	/**
	 * Produces a Dot file to draw the graph
	 * @return A string that can be passed to Dot
	 */
	public String toDot()
	{
		return toDot(Format.DOT);
	}
		
	/**
	 * Prints the vertex in Dot format
	 * @param v The vertex
	 * @param format The format to print out
	 * @return The Dot string
	 */
	protected String printVertex(Vertex<T> v, Format format)
	{
		return m_graph.m_labelling.get(v.getId()).toString();
	}

	/**
	 * Prints the edge in Dot format
	 * @param e The edge
	 * @param format The format to print out
	 * @return The Dot string
	 */
	protected String printEdge(Edge<T> e, Format format)
	{
		return e.getLabel().toString();
	}
		
	/**
	 * Prints the edges of the graph, by merging the label of all edges
	 * with the same target state
	 * @param edges The list of edges
	 * @param source_id The ID of the source state for all the labels
	 */
	protected String printEdges(Set<Edge<T>> edges, int source_id, Format format)
	{
		StringBuilder out = new StringBuilder();
		Map<Integer,String> edge_labels = new HashMap<Integer,String>();
		for (Edge<T> e : edges)
		{
			String edge_label = "";
			int destination = e.getDestination();
			if (edge_labels.containsKey(destination))
			{
				edge_label = edge_labels.get(destination);
			}
			edge_label += printEdge(e, format) + ",";
			edge_labels.put(destination, edge_label);
		}
		for (int dest : edge_labels.keySet())
		{
			String label = edge_labels.get(dest);
			label = label.substring(0, label.length() - 1);
			out.append(" ").append(source_id).append(" -> ").append(dest);
			out.append("[label=\"").append(label).append("\"];\n");
		}
		return out.toString();
	}
	
}
