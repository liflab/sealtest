package ca.uqac.lif.brutecount;

import java.util.HashSet;
import java.util.Set;

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
}
