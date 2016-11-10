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
import ca.uqac.lif.ecp.graphs.TreeCollector;
import ca.uqac.lif.ecp.graphs.UnionFindSpanningTree;

/**
 * Trace generator that uses a spanning tree of the Cayley graph. This
 * generator can achieve total coverage with respect to the equivalence class
 * coverage metric, <b>c<sub>e</sub></b>.
 * @author Sylvain Hallé
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
		TreeCollector<T> collector = new TreeCollector<T>(tree);
		TestSuite<T> traces = collector.getTraces();
		return traces;
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
