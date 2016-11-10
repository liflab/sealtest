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

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.ecp.Trace;

public class TreeCollector<T extends Event> 
{
	/**
	 * The Cayley graph from which to collect the results
	 */
	protected final LabelledGraph<T> m_graph;
	
	/**
	 * Maximum recursion depth
	 */
	public static final int s_maxDepth = 1000;
	
	public TreeCollector(LabelledGraph<T> graph)
	{
		super();
		m_graph = graph;
	}
	
	public TestSuite<T> getTraces()
	{
		TestSuite<T> traces = new TestSuite<T>();
		Vertex<T> root = m_graph.getInitialVertex();
		depthFirstCollect(m_graph, root, traces);
		return traces;
	}
	
	/**
	 * Collects all the root-to-leaf paths in a spanning tree
	 * @param graph The tree (it is actually an instance of Cayley graph)
	 * @param v The root of the spanning tree
	 * @param traces An empty set of traces that will be filled by the method
	 */
	protected void depthFirstCollect(LabelledGraph<T> graph, Vertex<T> v, TestSuite<T> traces)
	{
		depthFirstCollect(graph, v, traces, new Trace<T>(), s_maxDepth);
	}
	
	/**
	 * Collects all the root-to-leaf paths in a spanning tree
	 * @param graph The tree (it is actually an instance of Cayley graph)
	 * @param v The root of the spanning tree
	 * @param traces An empty set of traces that will be filled by the method
	 * @param current_trace The current trace, to which events will be appended.
	 *   The procedure starts with an empty trace
	 */
	protected void depthFirstCollect(LabelledGraph<T> graph, Vertex<T> v, TestSuite<T> traces, Trace<T> current_trace, int depth)
	{
		if (depth == 0 || v == null)
		{
			// Maximum recursion depth reached
			return;
		}
		if (v.isLeaf())
		{
			Trace<T> new_trace = new Trace<T>(current_trace);
			traces.add(new_trace);
			return;
		}
		for (Edge<T> edge : v.getEdges())
		{
			Vertex<T> target_vertex = graph.getVertex(edge.getDestination());
			current_trace.add(edge.getLabel());
			depthFirstCollect(graph, target_vertex, traces, current_trace, depth - 1);
			current_trace.remove(current_trace.size() - 1);
		}
	}
}
