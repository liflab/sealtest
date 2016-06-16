package ca.uqac.lif.brutecount;

import java.util.LinkedList;
import java.util.List;

public class Vertex
{
	protected String m_label;
	
	protected List<Edge> m_outEdges;
	
	public Vertex(String label)
	{
		super();
		m_label = label;
		m_outEdges = new LinkedList<Edge>();
	}
	
	public void add(Edge e)
	{
		m_outEdges.add(e);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_label).append("\n");
		for (Edge e : m_outEdges)
		{
			out.append("  ").append(e).append("\n");
		}
		return out.toString();
	}
	

}
