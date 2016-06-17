package ca.uqac.lif.pathcount;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph 
{
	public Set<Vertex> m_vertices;
	
	public Graph()
	{
		super();
		m_vertices = new HashSet<Vertex>();
	}
	
	public Vertex getVertex(String label)
	{
		for (Vertex v : m_vertices)
		{
			if (v.m_label.compareTo(label) == 0)
			{
				return v;
			}
		}
		return null;
	}
	
	public Graph add(Vertex v)
	{
		m_vertices.add(v);
		return this;
	}
	
	/**
	 * Creates a graph from a dot string
	 * @param input A scanner to a dot string
	 * @return The graph
	 */
	public static Graph parseDot(Scanner scanner)
	{
		Graph g = new Graph();
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
				Vertex from = g.getVertex(s_from);
				if (from == null)
				{
					from = new Vertex(s_from);
					g.add(from);
				}
				String s_to = mat.group(2).trim();
				Vertex to = g.getVertex(s_to);
				if (to == null)
				{
					to = new Vertex(s_to);
					g.add(to);
				}
				String[] labels = mat.group(3).trim().split(",");
				for (String label : labels)
				{
					Edge e = new Edge(label, s_to);
					from.add(e);
				}
			}
		}
		scanner.close();
		return g;
	}
}
