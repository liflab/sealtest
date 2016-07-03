package ca.uqac.lif.ecp.lab;

import java.util.Scanner;

import ca.uqac.lif.ecp.AtomicEvent;
import ca.uqac.lif.ecp.CountVisitor;
import ca.uqac.lif.ecp.Vertex;
import ca.uqac.lif.json.JsonList;

public class PathCountExperiment extends GraphExperiment 
{
	public PathCountExperiment(Scanner scanner, int max_length)
	{
		super(scanner);
		setDescription("This experiment counts the number of paths ending in each state of the automaton for the property '" + readString("property") + "'");
		describe("max-length", "Upper bound on the length of the paths to enumerate");
		setInput("max-length", max_length);
		write("max-length", max_length);
		JsonList len_list = new JsonList();
		for (int i = 0; i <= max_length; i++)
		{
			len_list.add(i);
		}
		describe("length", "The list of path lengths");
		write("length", len_list);
		for (Vertex<AtomicEvent> v : m_graph.m_vertices)
		{
			JsonList list = new JsonList();
			for (int i = 0; i <= max_length; i++)
			{
				list.add(0);
			}
			String s_id = Integer.toString(v.getId());
			describe(s_id, "The number of paths ending in state " + v.getId() + " for increasing lengths");
			write(s_id, list);
		}
	}
		
	@Override
	public Status execute() 
	{
		if (m_graph == null)
		{
			setErrorMessage("Could not find resource");
			return Status.FAILED;
		}
		CountVisitor<AtomicEvent> cv = new CountVisitor<AtomicEvent>(this);
		cv.start(m_graph, 0, readInt("max-length"));
		return Status.DONE;
	}

}
