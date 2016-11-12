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
package ca.uqac.lif.ecp.atomic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CayleyVertexLabelling;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

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
	 * The alphabet associated to this automaton
	 */
	protected Alphabet<AtomicEvent> m_alphabet;
	
	public Automaton()
	{
		super();
		m_alphabet = new Alphabet<AtomicEvent>();
	}
	
	/**
	 * Creates an automaton from a dot string
	 * @param input A dot string
	 * @return The graph
	 */
	public static Automaton parseDot(String s)
	{
		return parseDot(new Scanner(s));
	}
	
	/**
	 * Sets the alphabet for this automaton
	 * @param alphabet The alphabet
	 */
	public void setAlphabet(Alphabet<AtomicEvent> alphabet)
	{
		m_alphabet = alphabet;
	}
	
	@Override
	public void add(Vertex<AtomicEvent> v)
	{
		super.add(v);
		// Look into the vertex's edges to update the alphabet
		for (Edge<AtomicEvent> e : v.getEdges())
		{
			AtomicEvent ae = e.getLabel();
			if (!(ae instanceof ElseEvent))
			{
				m_alphabet.add(e.getLabel());
			}
		}
	}
	
	/**
	 * Creates an automaton from a dot string
	 * @param input A scanner to a dot string
	 * @return The graph
	 */
	public static Automaton parseDot(Scanner scanner)
	{
		Automaton g = new Automaton();
		Map<String,AtomicEvent> event_pool = new HashMap<String,AtomicEvent>();
		Alphabet<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
		CayleyVertexLabelling<String> labelling = new CayleyVertexLabelling<String>();
		Pattern pat_edge = Pattern.compile("(.*?)->(.*?) \\[label=\"(.*?)\"\\];");
		Pattern pat_vertex = Pattern.compile("(\\d+?) \\[label=\"(.*?)\"\\];");
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			line = line.trim();
			if (line.isEmpty() || line.startsWith(("#")))
				continue;
			Matcher mat = pat_edge.matcher(line);
			if (mat.find())
			{
				// New edge
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
					Edge<AtomicEvent> e;
					if (label.compareTo(ElseEvent.label) == 0)
					{
						// "Else" transition
						e = new Edge<AtomicEvent>(i_from, ElseEvent.instance, i_to);
					}
					else
					{
						// Normal transition
						AtomicEvent ae = null;
						if (event_pool.containsKey(label))
						{
							// We reuse the instance of atomic event created for this label
							ae = event_pool.get(label);
						}
						else
						{
							ae = new AtomicEvent(label);
							event_pool.put(label, ae);
							alphabet.add(ae);
						}
						e = new Edge<AtomicEvent>(i_from, ae, i_to);
					}
					from.add(e);
				}
			}
			else
			{
				// New vertex
				mat = pat_vertex.matcher(line);
				if (!mat.find())
				{
					// Ignore line
					continue;
				}
				int vertex_id = Integer.parseInt(mat.group(1));
				String[] vertex_labels = mat.group(2).split(",");
				MathSet<String> labels = new MathSet<String>();
				for (String label : vertex_labels)
				{
					labels.add(label);
				}
				labelling.put(vertex_id, labels);
			}
		}
		g.setLabelling(labelling);
		g.setAlphabet(alphabet);
		scanner.close();
		return g;
	}
	
	/**
	 * Gets the set of all symbols occurring on edge labels in the graph.
	 * @return The alphabet
	 */
	public Alphabet<AtomicEvent> getAlphabet()
	{
		return m_alphabet;
	}
	
	/**
	 * Takes a transition from a given state
	 * @param current_vertex The current state
	 * @param event The event
	 * @return The edge representing the transition to take
	 */
	public Edge<AtomicEvent> getTransition(Vertex<AtomicEvent> current_vertex, AtomicEvent event)
	{
		Edge<AtomicEvent> else_edge = null;
		for (Edge<AtomicEvent> edge : current_vertex.getEdges())
		{
			AtomicEvent label = edge.getLabel();
			if (label instanceof ElseEvent)
			{
				// Remember "else" edge in case nothing else matches
				else_edge = edge;
			}
			else if (edge.getLabel().equals(event))
			{
				// Labels match: this is the edge
				return edge;
			}
		}
		if (else_edge != null)
		{
			// No match, but "else" edge exists: take it
			return else_edge;
		}
		// This should not happen in the transition function is complete
		return null;
	}
}
