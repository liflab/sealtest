package ca.uqac.lif.ecp.atomic;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.Vertex;

/**
 * Implementation of a deterministic finite-state automaton. For the 
 * sake of this project, the FSA is simply viewed as a special case of
 * Cayley graph whose labelling is irrelevant.
 *  
 * @author Sylvain
 *
 */
public class Automaton extends AtomicCayleyGraph<String>
{
	
	/**
	 * Creates an automaton from a dot string
	 * @param input A scanner to a dot string
	 * @return The graph
	 */
	public static Automaton parseDot(Scanner scanner)
	{
		Automaton g = new Automaton();
		Pattern pat = Pattern.compile("(.*?)->(.*?) \\[label=\"(.*?)\"\\];");
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			line = line.trim();
			if (line.isEmpty() || line.startsWith(("#")))
				continue;
			Matcher mat = pat.matcher(line);
			if (mat.find())
			{
				String s_from = mat.group(1).trim();
				int i_from = Integer.parseInt(s_from);
				Vertex<AtomicEvent> from = g.getVertex(i_from);
				if (from == null)
				{
					from = new Vertex<AtomicEvent>(i_from);
					g.add(from);
				}
				String s_to = mat.group(2).trim();
				int i_to = Integer.parseInt(s_to);
				Vertex<AtomicEvent> to = g.getVertex(i_to);
				if (to == null)
				{
					to = new Vertex<AtomicEvent>(i_to);
					g.add(to);
				}
				String[] labels = mat.group(3).trim().split(",");
				for (String label : labels)
				{
					Edge<AtomicEvent> e = new Edge<AtomicEvent>(i_from, new AtomicEvent(label), i_to);
					from.add(e);
				}
			}
		}
		scanner.close();
		return g;
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
			for (Edge<AtomicEvent> edge : current_vertex.getEdges())
			{
				if (edge.getLabel().equals(event))
				{
					int destination_id = edge.getDestination();
					Vertex<AtomicEvent> target_vertex = getVertexWithId(destination_id);
					visitor.visit(current_vertex, edge);
					current_vertex = target_vertex;
					break;
				}
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
			for (Edge<AtomicEvent> e : v.getEdges())
			{
				alphabet.add(e.getLabel());
			}
			// Since the transition relation is supposed to be total, we
			// have found all the symbols by looking at the outgoing edges
			// of a single vertex
			break;
		}
		return alphabet;
	}
}
