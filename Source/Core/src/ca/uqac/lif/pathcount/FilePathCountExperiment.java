package ca.uqac.lif.pathcount;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.json.JsonList;

public class FilePathCountExperiment extends PathCountExperiment 
{
	FilePathCountExperiment()
	{
		super();
	}
	
	public FilePathCountExperiment(Scanner scanner, int max_length)
	{
		super();
		write("max-length", max_length);
		Pattern pat_title = Pattern.compile("Title: (.*)");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.startsWith("digraph"))
				break;
			Matcher mat = pat_title.matcher(line);
			if (mat.find())
			{
				setDescription("This experiment counts the number of paths ending in each state of the automaton for the property '" + mat.group(1) + "'");
				setInput("property", mat.group(1));
			}
		}
		Graph g = Graph.parseDot(scanner);
		m_graph = g;
		JsonList len_list = new JsonList();
		for (int i = 0; i <= max_length; i++)
		{
			len_list.add(i);
		}
		describe("length", "The list of path lengths");
		write("length", len_list);
		for (Vertex v : g.m_vertices)
		{
			JsonList list = new JsonList();
			for (int i = 0; i <= max_length; i++)
			{
				list.add(0);
			}
			describe(v.m_label, "The number of paths ending in state " + v.m_label + " for increasing lengths");
			write(v.m_label, list);
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
		CountVisitor cv = new CountVisitor(this);
		cv.start(m_graph, "0", readInt("max-length"));
		return Status.DONE;
	}

}
