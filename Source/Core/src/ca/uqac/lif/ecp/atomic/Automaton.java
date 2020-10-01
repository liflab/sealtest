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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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

	/**
	 * A title to give to the automaton
	 */
	protected String m_title = "";

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
	 * Gives a title to the automaton
	 * @param title The title
	 */
	public void setTitle(String title)
	{
		m_title = title;
	}

	/**
	 * Gets the title of this automaton
	 * @return The title
	 */
	public String getTitle()
	{
		return m_title;
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

	public void add(@SuppressWarnings("unchecked") Vertex<AtomicEvent> ... vertices)
	{
		for (Vertex<AtomicEvent> v : vertices)
		{
			add(v);
		}
	}

	/**
	 * Creates an automaton from a dot string
	 * @param input A scanner to a dot string
	 * @return The graph
	 */
	public static Automaton parseDot(Scanner scanner)
	{
		return parseDot(scanner, "");
	}

	/**
	 * Creates an automaton from a dot string
	 * @param input A scanner to a dot string
	 * @param title A title to give to the automaton
	 * @return The graph
	 */
	public static Automaton parseDot(Scanner scanner, String title)
	{
		Automaton g = new Automaton();
		g.setTitle(title);
		Map<String,AtomicEvent> event_pool = new HashMap<String,AtomicEvent>();
		Alphabet<AtomicEvent> alphabet = new Alphabet<AtomicEvent>();
		CayleyVertexLabelling<String> labelling = new CayleyVertexLabelling<String>();
		Map<String,Vertex<AtomicEvent>> vertices = new HashMap<String,Vertex<AtomicEvent>>();
		Pattern pat_edge = Pattern.compile("(.*?)->(.*?) \\[label=[\"<](.*?)[\">]\\];{0,1}");
		Pattern pat_vertex = Pattern.compile("([^\\s]+?) \\[([^\\]]*?)\\];{0,1}");
		Pattern pat_label = Pattern.compile("label=[\"<](.*?)[\">]");
		Set<String> to_ignore = new HashSet<String>();
		int initial_id = -1;
		int id_counter = 0;
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
				if (!vertices.containsKey(s_from))
				{
					Vertex<AtomicEvent> v = new Vertex<AtomicEvent>(id_counter++);
					vertices.put(s_from, v);
				}
				int i_from = vertices.get(s_from).getId();
				String s_to = mat.group(2).trim();
				if (!vertices.containsKey(s_to))
				{
					Vertex<AtomicEvent> v = new Vertex<AtomicEvent>(id_counter++);
					vertices.put(s_to, v);
				}
				int i_to = vertices.get(s_to).getId();
				String unformatted_label = mat.group(3).trim();
				if (unformatted_label.startsWith("\\\""))
				{
					// Label is surrounded by spurious escaped quotes: remove
					unformatted_label = unformatted_label.replace("\\\"", "");
				}
				String[] labels = unformatted_label.split(",");
				for (String label : labels)
				{
					if (label.compareToIgnoreCase("START") == 0)
					{
						// In some graph formats, the START transition points to
						// the initial state of the graph. The incoming vertex of
						// this transition is invisible and will be deleted from
						// the final graph
						initial_id = i_to;
						continue;
					}
					if (initial_id == -1)
					{
						initial_id = i_from;
					}
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
					vertices.get(s_from).add(e);
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
				// Replace all nondigits by blank, and then parse int
				String vertex_string = mat.group(1).trim();
				if (!vertices.containsKey(vertex_string))
				{
					Vertex<AtomicEvent> v = new Vertex<AtomicEvent>(id_counter++);
					vertices.put(vertex_string, v);
				}
				int vertex_id = vertices.get(vertex_string).getId();
				String vertex_attributes = mat.group(2);
				if (vertex_attributes.contains("invis"))
				{
					// Node is invisible: will be ignored
					to_ignore.add(mat.group(1));
					continue;
				}
				if (vertex_string.compareToIgnoreCase("invalid") == 0)
				{
					labelling.put(vertex_id, new MathSet<String>("invalid"));
				}
				else
				{
					Matcher label_mat = pat_label.matcher(vertex_attributes);
					if (label_mat.find())
					{
						String[] vertex_labels = label_mat.group(1).split(",");
						MathSet<String> labels = new MathSet<String>();
						for (String label : vertex_labels)
						{
							labels.add(label);
						}
						labelling.put(vertex_id, labels);					
					}
					else
					{
						// By default: the label is just the parsed ID
						MathSet<String> labels = new MathSet<String>(Integer.toString(vertex_id));
						labelling.put(vertex_id, labels);
					}					
				}
			}
		}
		for (String name : to_ignore)
		{
			vertices.remove(name);
		}
		for (Vertex<AtomicEvent> v : vertices.values())
		{
			g.add(v);
		}
		if (initial_id != -1)
		{
			g.setInitialVertexId(initial_id);
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

	/**
	 * Modifies the transition function of the automaton by adding a sink state
	 * and putting an "else" transition from every state to this sink. This has
	 * for effect that the transition function becomes total.
	 */
	public void makeTotal()
	{
		int id = getVertexCount();
		Vertex<AtomicEvent> invalid = new Vertex<AtomicEvent>(id);
		boolean sink_needed = false;
		invalid.add(new Edge<AtomicEvent>(id, ElseEvent.instance, id));
		for (Vertex<AtomicEvent> v : getVertices())
		{
			boolean has_else = false;
			Set<AtomicEvent> outgoing = new HashSet<AtomicEvent>();
			for (Edge<AtomicEvent> e : v.getEdges())
			{
				if (e.getLabel().getLabel().compareToIgnoreCase(ElseEvent.label) == 0)
				{
					has_else = true;
					break;
				}
				outgoing.add(e.getLabel());
			}
			if (!has_else && outgoing.size() < m_alphabet.size())
			{
				v.add(new Edge<AtomicEvent>(v.getId(), ElseEvent.instance, id));
				sink_needed = true;
			}
		}
		if (sink_needed)
		{
			add(invalid);
		}
	}

	/**
	 * On each state, replaces any "else" transition by concrete transitions
	 * for all labels except those already present on outgoing transitions.
	 */
	public void replaceElse()
	{
		for (Vertex<AtomicEvent> v : getVertices())
		{
			int else_dest = -1;
			Alphabet<AtomicEvent> e_a = new Alphabet<AtomicEvent>();
			e_a.addAll(m_alphabet);
			Iterator<Edge<AtomicEvent>> it = v.getEdges().iterator();
			while (it.hasNext())
			{
				Edge<AtomicEvent> e = it.next();
				if (e.getLabel().getLabel().compareToIgnoreCase(ElseEvent.label) == 0)
				{
					else_dest = e.getDestination();
					it.remove();
				}
				else
				{
					e_a.remove(e.getLabel());
				}
			}
			for (AtomicEvent ae : e_a)
			{
				v.add(new Edge<AtomicEvent>(v.getId(), ae, else_dest));
			}
		}
	}
}
