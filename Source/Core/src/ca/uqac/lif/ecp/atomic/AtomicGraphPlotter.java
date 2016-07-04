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
package ca.uqac.lif.ecp.atomic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.GraphPlotter;

/**
 * Graph plotter for Cayley graphs made of atomic events
 *
 * @param <U> The output type of the triaging function used to create
 *   the graph 
 */
public class AtomicGraphPlotter<U extends Object> extends GraphPlotter<AtomicEvent,U> 
{
	public AtomicGraphPlotter(CayleyGraph<AtomicEvent, U> graph)
	{
		super(graph);
	}
	
	/**
	 * Prints the edges of the graph, by merging the label of all edges
	 * with the same target state
	 * @param edges The list of edges
	 * @param source_id The ID of the source state for all the labels
	 */
	protected String printEdges(List<Edge<AtomicEvent>> edges, int source_id, Format format)
	{
		StringBuilder out = new StringBuilder();
		Map<Integer,String> edge_labels = new HashMap<Integer,String>();
		for (Edge<AtomicEvent> e : edges)
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
