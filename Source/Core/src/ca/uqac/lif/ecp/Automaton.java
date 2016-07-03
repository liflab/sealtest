package ca.uqac.lif.ecp;

import java.util.Scanner;

/**
 * Implementation of a deterministic finite-state automaton. For the 
 * sake of this project, the FSA is simply viewed as a special case of
 * Cayley graph whose labelling is irrelevant.
 *  
 * @author Sylvain
 *
 */
public class Automaton extends AtomicCayleyGraph
{
	public static Automaton parseDot(Scanner scanner)
	{
		AtomicCayleyGraph acg = AtomicCayleyGraph.parseDot(scanner);
		Automaton aut = new Automaton();
		aut.m_vertices = acg.m_vertices;
		aut.m_labelling = acg.m_labelling;
		return aut;
	}
	
	/**
	 * Reads a trace, and calls a visitor for every transition taken
	 * @param trace The trace to read
	 * @param visitor The visitor to call
	 */
	public void read(Trace<AtomicEvent> trace, AutomatonVisitor visitor)
	{
		Vertex<AtomicEvent> current_vertex = getVertexWithId(0);
		visitor.visit(current_vertex, null); // Call once for the initial state
		for (AtomicEvent event : trace)
		{
			for (Edge<AtomicEvent> edge : current_vertex.m_outEdges)
			{
				if (edge.m_label.equals(event))
				{
					int destination_id = edge.m_destination;
					Vertex<AtomicEvent> target_vertex = getVertexWithId(destination_id);
					visitor.visit(current_vertex, edge);
					current_vertex = target_vertex;
					break;
				}
				// Not supposed to get here!
				assert false;
			}
		}
	}
	
	/**
	 * Gets the set of all symbols occurring on edge labels in the graph.
	 * @return The alphabet
	 */
	public Alphabet<AtomicEvent> getAlphabet()
	{
		Alphabet<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
		for (Vertex<AtomicEvent> v : m_vertices)
		{
			for (Edge<AtomicEvent> e : v.m_outEdges)
			{
				alphabet.add(e.m_label);
			}
			// Since the transition relation is supposed to be total, we
			// have found all the symbols by looking at the outgoing edges
			// of a single vertex
			break;
		}
		return alphabet;
	}
}
