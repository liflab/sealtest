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

import ca.uqac.lif.ecp.graphs.SpanningTree;
import ca.uqac.lif.ecp.graphs.UnionFindSpanningTree;
import ca.uqac.lif.ecp.graphs.Vertex;

/**
 * Trace generator that uses a spanning tree of the Cayley graph. This
 * generator can achieve total coverage with respect to the equivalence class
 * coverage metric, <b>c<sub>e</sub></b>.
 * @author Sylvain Hall�
 *
 * @param <T>
 * @param <U>
 */
public class SpanningTreeTraceGenerator<T extends Event,U extends Object> extends CayleyGraphTraceGenerator<T,U> 
{	
	/**
	 * The spanning tree obtained from this Cayley graph
	 */
	protected CayleyGraph<T,U> m_tree = null;
	
	/**
	 * The solver that will resolve the underlying spanning tree problem
	 */
	protected SpanningTree<T,U> m_solver;
	
	/**
	 * Creates a new spanning tree generator
	 * @param graph The Cayley graph used to generate the traces
	 */
	public SpanningTreeTraceGenerator(CayleyGraph<T,U> graph)
	{
		super(graph);
		// This is currently the only spanning tree solver we have
		m_solver = new UnionFindSpanningTree<T,U>(graph);
	}
	
	/**
	 * Creates a new spanning tree generator
	 * @param graph The Cayley graph used to generate the traces
	 */
	public SpanningTreeTraceGenerator(CayleyGraph<T,U> graph, SpanningTree<T,U> solver)
	{
		super(graph);
		m_solver = solver;
		m_solver.setGraph(graph);
	}

	@Override
	public TestSuite<T> generateTraces()
	{
		CayleyGraph<T,U> tree = getSpanningTree();
		TestSuite<T> traces = new TestSuite<T>();
		Vertex<T> root = tree.getInitialVertex();
		depthFirstCollect(tree, root, traces);
		return traces;
	}

	/**
	 * Collects all the root-to-leaf paths in a spanning tree
	 * @param graph The tree (it is actually an instance of Cayley graph)
	 * @param v The root of the spanning tree
	 * @param traces An empty set of traces that will be filled by the method
	 */
	protected void depthFirstCollect(CayleyGraph<T,U> graph, Vertex<T> v, TestSuite<T> traces)
	{
		depthFirstCollect(graph, v, traces, new Trace<T>());
	}
	
	/**
	 * Collects all the root-to-leaf paths in a spanning tree
	 * @param graph The tree (it is actually an instance of Cayley graph)
	 * @param v The root of the spanning tree
	 * @param traces An empty set of traces that will be filled by the method
	 * @param current_trace The current trace, to which events will be appended.
	 *   The procedure starts with an empty trace
	 */
	protected void depthFirstCollect(CayleyGraph<T,U> graph, Vertex<T> v, TestSuite<T> traces, Trace<T> current_trace)
	{
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
			// We are lazy and generate the spanning tree only if we don't
			// already have one
			return m_tree;
		}
		m_tree = m_solver.getSpanningTree();
		return m_tree;
	}
}
