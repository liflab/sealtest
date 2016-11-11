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

import ca.uqac.lif.ecp.graphs.DijkstraSteinerTree;
import ca.uqac.lif.ecp.graphs.GreedyHypergraphEdgeCover;
import ca.uqac.lif.ecp.graphs.Hypergraph;
import ca.uqac.lif.ecp.graphs.Hypergraph.Hyperedge;
import ca.uqac.lif.ecp.graphs.HypergraphEdgeCover;
import ca.uqac.lif.ecp.graphs.SteinerTree;
import ca.uqac.lif.ecp.graphs.TreeCollector;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathMap;
import ca.uqac.lif.structures.MathSet;

/**
 * Trace generator that uses the hypergraph&nbsp;+&nbsp;Steiner tree method. 
 * This generator can achieve total coverage with respect to the category
 * coverage metric, <b>c<sub>c</sub></b>.
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class HypergraphTraceGenerator<T extends Event,U extends Object> extends CayleyGraphTraceGenerator<T,U> 
{
	/**
	 * Solver used for the hypergraph edge cover problem
	 */
	protected HypergraphEdgeCover m_hypergraphSolver;
	
	/**
	 * Solver used for the directed Steiner tree problem
	 */
	protected SteinerTree<T,U> m_dstSolver;
	
	/**
	 * Creates a new trace generator
	 * @param graph The Cayley graph to use as the input
	 */
	public HypergraphTraceGenerator(CayleyGraph<T,U> graph) 
	{
		super(graph);
		m_hypergraphSolver = new GreedyHypergraphEdgeCover(null);
		m_dstSolver = new DijkstraSteinerTree<T,U>(null, null);
	}

	@Override
	public TestSuite<T> generateTraces() 
	{
		// Create a hypergraph out of the Cayley graph
		MathMap<Integer,U> hyper_labelling = new MathMap<Integer,U>();
		Hypergraph hg = createHypergraph(hyper_labelling);
		// Select the important vertices
		HypergraphEdgeCover h_solver = m_hypergraphSolver.newSolver(hg);
		
		MathSet<Hyperedge> hyperedges = h_solver.getCover();
		System.err.println("Cover contains " + hyperedges.size() + " hyperedges");
		// Find a vertex in the Cayley graph for each of the hyperedges
		MathSet<Integer> important_vertices = new MathSet<Integer>();
		for (Hyperedge he : hyperedges)
		{
			MathSet<U> equiv_class = getEquivalenceClass(he, hyper_labelling);
			Vertex<T> found_vertex = m_graph.getFirstVertexWithLabelling(equiv_class);
			important_vertices.add(found_vertex.getId());
		}
		System.err.println("Get Steiner tree");
		// Get a set of traces reaching these vertices
		SteinerTree<T,U> bfst = m_dstSolver.newSolver(m_graph, important_vertices);
		CayleyGraph<T,U> tree = bfst.getTree();
		System.err.println("Get traces from tree");
		TreeCollector<T> collector = new TreeCollector<T>(tree);
		TestSuite<T> suite = collector.getTraces();
		System.err.println("Done");
		return suite;
	}
	
	/**
	 * Gets the equivalence class corresponding to a hyperedge
	 * @param he The hyperedge
	 * @param hyper_labelling A mapping from hypergraph edge IDs to
	 *   categories
	 * @return The equivalence class
	 */
	protected MathSet<U> getEquivalenceClass(Hyperedge he, MathMap<Integer,U> hyper_labelling)
	{
		MathSet<U> equiv_class = new MathSet<U>();
		for (Integer hyper_id : he)
		{
			U category = hyper_labelling.get(hyper_id);
			equiv_class.add(category);
		}
		return equiv_class;
	}
	
	/**
	 * Creates a hypergraph out of the Cayley graph. The vertices
	 * of the hypergrph are each category of the triaging function.
	 * There is one hyperedge for each equivalence class
	 * @param hyper_labelling An initially empty labelling that maps
	 *   vertex IDs of the hypergraph to categories of the original
	 *   Cayley graph
	 * @return The hypergraph
	 */
	Hypergraph createHypergraph(MathMap<Integer,U> hyper_labelling)
	{
		CayleyVertexLabelling<U> labelling = m_graph.getLabelling();
		Hypergraph g = new Hypergraph();
		for (Vertex<T> vertex : m_graph.getVertices())
		{
			Hyperedge he = new Hyperedge();
			int vertex_id = vertex.getId();
			MathSet<U> label = labelling.get(vertex_id);
			for (U category : label)
			{
				Integer hyper_vertex_id = hyper_labelling.getWithValue(category);
				if (hyper_vertex_id == null)
				{
					// Add this category
					hyper_vertex_id = g.newVertex();
					hyper_labelling.put(hyper_vertex_id, category);
				}
				he.add(hyper_vertex_id);
			}
			g.addEdge(he);
		}
		return g;
	}

}
