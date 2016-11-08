package ca.uqac.lif.ecp;

import ca.uqac.lif.ecp.graphs.BreadthFirstSteinerTree;
import ca.uqac.lif.ecp.graphs.Hypergraph;
import ca.uqac.lif.ecp.graphs.Hypergraph.Hyperedge;
import ca.uqac.lif.ecp.graphs.HypergraphEdgeCover;
import ca.uqac.lif.ecp.graphs.SteinerTree;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * Trace generator that uses the hypergraph&nbsp;+&nbsp;Steiner tree method. 
 * This generator can achieve total coverage with respect to the category
 * coverage metric, <b>c<sub>c</sub></b>.
 * @author Sylvain Hall�
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
	
	public HypergraphTraceGenerator(CayleyGraph<T, U> graph) 
	{
		super(graph);
	}

	@Override
	public TestSuite<T> generateTraces() 
	{
		// Create a hypergraph out of the Cayley graph
		Hypergraph hg = createHypergraph();
		// Select the important vertices
		HypergraphEdgeCover h_solver = m_hypergraphSolver.newSolver(hg);
		MathSet<Hyperedge> hyperedges = h_solver.getCover();
		// Find a vertex in the Cayley graph for each of the hyperedges
		MathSet<Vertex<T>> important_vertices = new MathSet<Vertex<T>>();
		/*for (Hyperedge he : hyperedges)
		{
			
			Vertex<T> found_vertex = m_graph.getFirstVertexWithLabelling(he);
		}
		return null;
		// Get a set of traces reaching these hyperedges
		SteinerTree<T,U> bfst = new BreadthFirstSteinerTree<T,U>(m_graph);
		bfst.getTree();*/
		return null;
	}
	
	Hypergraph createHypergraph()
	{
		//Hypergraph g = new Hypergraph();
		return null;
	}

}
