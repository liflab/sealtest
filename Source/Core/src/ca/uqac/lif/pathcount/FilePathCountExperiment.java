package ca.uqac.lif.pathcount;

import java.io.InputStream;
import java.util.Scanner;

import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.parkbench.FileHelper;

public class FilePathCountExperiment extends PathCountExperiment 
{
	protected static transient final String s_dataPath = "data/";
	
	protected transient Graph m_graph;
	
	FilePathCountExperiment()
	{
		super();
		describe("filename", "Name of the file containing te automaton");
	}
	
	public FilePathCountExperiment(String property, String filename, int max_length)
	{
		super(property, max_length);
		describe("filename", "Name of the file containing te automaton");
		setInput("filename", filename);
		InputStream is = FileHelper.internalFileToStream(FilePathCountExperiment.class, s_dataPath + readString("filename"));
		if (is == null)
		{
			m_graph = null;
			return;
		}
		Scanner scanner = new Scanner(is);
		Graph g = Graph.parseDot(scanner);
		m_graph = g;
		JsonList len_list = new JsonList();
		for (int i = 0; i <= max_length; i++)
		{
			len_list.add(i);
		}
		write("length", len_list);
		for (Vertex v : g.m_vertices)
		{
			JsonList list = new JsonList();
			for (int i = 0; i <= max_length; i++)
			{
				list.add(0);
			}
			write(v.m_label, list);
		}
	}
	
	@Override
	public Status execute() 
	{
		if (m_graph == null)
		{
			setErrorMessage("Could not find resource " + s_dataPath + readString("filename"));
			return Status.FAILED;
		}
		CountVisitor cv = new CountVisitor(this);
		cv.start(m_graph, "0", readInt("max-length"));
		return Status.DONE;
	}
	
	public Graph getGraph()
	{
		return m_graph;
	}

}
