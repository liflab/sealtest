package ca.uqac.lif.brutecount;

public class Edge 
{
	protected String m_label;
	
	protected String m_destination;
	
	public Edge(String label, String destination)
	{
		super();
		m_label = label;
		m_destination = destination;
	}
	
	@Override
	public String toString()
	{
		return m_label + "->" + m_destination;
	}
}
