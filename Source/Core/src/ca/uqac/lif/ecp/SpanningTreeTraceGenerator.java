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

import java.util.Iterator;
import java.util.List;

public class SpanningTreeTraceGenerator<T extends Event,U extends Object> extends TraceGenerator<T> 
{
	/**
	 * The Cayley graph used to generate the traces
	 */
	protected final CayleyGraph<T,U> m_graph;
	
	/**
	 * The spanning tree obtained from this Cayley graph
	 */
	protected CayleyGraph<T,U> m_tree = null;
	
	/**
	 * Creates a new spanning tree generator
	 * @param graph The Cayley graph used to generate the traces
	 */
	public SpanningTreeTraceGenerator(CayleyGraph<T,U> graph)
	{
		super();
		m_graph = graph;
	}

	@Override
	public TestSuite<T> generateTraces()
	{
		CayleyGraph<T,U> tree = getSpanningTree();
		TestSuite<T> traces = new TestSuite<T>();
		Trace<T> start_trace = new Trace<T>();
		Vertex<T> root = tree.getInitialVertex();
		depthFirstCollect(tree, root, traces, start_trace);
		return traces;
	}
	
	protected void depthFirstCollect(CayleyGraph<T,U> graph, Vertex<T> v, TestSuite<T> traces, Trace<T> current_trace)
	{
		if (v.isLeaf())
		{
			Trace<T> new_trace = new Trace<T>(current_trace);
			traces.add(new_trace);
			return;
		}
		for (Edge<T> edge : v.m_outEdges)
		{
			Vertex<T> target_vertex = graph.getVertex(edge.getDestination());
			current_trace.add(edge.getLabel());
			depthFirstCollect(graph, target_vertex, traces, current_trace);
			current_trace.remove(current_trace.size() - 1);
		}
	}
	
	/**
	 * Creates the minimum spanning tree of the Cayley graph
	 * @return A subset of the original Cayley graph that is a spanning
	 *   tree
	 */
	public CayleyGraph<T,U> getSpanningTree()
	{
		if (m_tree != null)
		{
			// We generate the spanning tree only on demand
			return m_tree;
		}
		ForestNode<T> forest = new ForestNode<T>(null);
		List<Edge<T>> edges = forest.getOrderedSpanningTree(m_graph);
		CayleyGraph<T,U> tree = new CayleyGraph<T,U>(m_graph);
		for (Vertex<T> v : tree.getVertices())
		{
			Iterator<Edge<T>> e_it = v.getEdges().iterator();
			while (e_it.hasNext())
			{
				Edge<T> e = e_it.next();
				if (!edges.contains(e))
				{
					// This edge is not in the list; take it off
					e_it.remove();
				}
			}
		}
		m_tree = tree;
		return tree;
	}
}
