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
package ca.uqac.lif.ecp.graphs;

import java.util.Collection;
import java.util.Scanner;

import ca.uqac.lif.structures.MathSet;

/**
 * Implementation of an undirected hypergraph
 * @author Sylvain Hall�
 */
public class Hypergraph 
{
	/**
	 * The set of hyperedges of the graph
	 */
	public MathSet<Hyperedge> m_edges;
	
	/**
	 * The set of vertices of the graph
	 */
	public MathSet<Integer> m_vertices;
	
	/**
	 * Creates a new empty hypergraph
	 */
	public Hypergraph()
	{
		super();
		m_edges = new MathSet<Hyperedge>();
		m_vertices = new MathSet<Integer>();
	}
	
	/**
	 * Adds a hyperedge to the hypergraph
	 * @param e The hyperedge
	 */
	public void addEdge(Hyperedge e)
	{
		m_edges.add(e);
		m_vertices.addAll(e);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_edges);
		return out.toString();
	}
	
	/**
	 * Gets the number of hyperedges in the graph
	 * @return The number
	 */
	public int getEdgeCount()
	{
		return m_edges.size();
	}
	
	/**
	 * Gets the number of vertices in the graph
	 * @return The number
	 */
	public int getVertexCount()
	{
		return m_vertices.size();
	}
	
	/**
	 * Creates a hypergraph from a character string.
	 * @see #parse(Scanner)
	 * @param s The string
	 * @return A hypergraph, or null if the graph could not be
	 *   created
	 */
	public static Hypergraph parse(String s)
	{
		Scanner scanner = new Scanner(s);
		return parse(scanner);
	}
	
	/**
	 * Creates a hypergraph from a stream of characters. The character string
	 * should be formatted like this:
	 * <pre>
	 * # Empty lines and lines that start with '#' are ignored
	 * # The remaining lines should contain a comma-separated list of
	 * # vertex numbers (integers)
	 * 0,1,2,3
	 * 1,2,4
	 * 2,3,6,1
	 * ...
	 * </pre>
	 * @param s The string, formatted as specified above
	 * @return A hypergraph, or null if the graph could not be
	 *   created
	 */
	public static Hypergraph parse(Scanner scanner)
	{
		Hypergraph g_out = new Hypergraph();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			String[] vertices = line.split(",");
			Hyperedge edge = new Hyperedge();
			for (String v : vertices)
			{
				int v_id = Integer.parseInt(v);
				edge.add(v_id);
			}
			g_out.addEdge(edge);
		}
		return g_out;
	}

	public static class Hyperedge extends MathSet<Integer>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Creates a new hyperedge, giving the vertices it contains
		 * @param vertices The vertices
		 */
		public Hyperedge(int ... vertices)
		{
			super();
			for (int vertex : vertices)
			{
				add(vertex);
			}
		}

		/**
		 * Creates a new hyperedge, giving the vertices it contains
		 * @param vertices The vertices
		 */
		public Hyperedge(Collection<Integer> vertices)
		{
			super();
			addAll(vertices);
		}
	}
}
