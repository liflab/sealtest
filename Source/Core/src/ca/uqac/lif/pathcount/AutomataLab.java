package ca.uqac.lif.pathcount;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import ca.uqac.lif.parkbench.CliParser;
import ca.uqac.lif.parkbench.CliParser.Argument;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.plot.BarPlot;
import ca.uqac.lif.parkbench.plot.Scatterplot;
import ca.uqac.lif.parkbench.table.ExperimentTable;
import ca.uqac.lif.parkbench.table.NormalizeRows;
import ca.uqac.lif.parkbench.table.Table;
import ca.uqac.lif.parkbench.Laboratory;

@SuppressWarnings("unused")
public class AutomataLab extends Laboratory
{
	protected static transient final String s_dataPath = "data/";

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		initialize(args, AutomataLab.class);
	}
	
	@Override
	public void setupCli(CliParser parser)
	{
		parser.addArgument(new Argument().withArgument("length").withLongName("max-length"));
	}
	
	@Override
	public void setupExperiments(ArgumentMap map)
	{
		int max_length = 10;
		if (map.hasOption("max-length"))
		{
			max_length = Integer.parseInt(map.getOptionValue("max-length"));
		}
		// Give a name to the lab
		setTitle("Path Counting");
		
		// Setup the experiments
		URL url = AutomataLab.class.getResource(s_dataPath);
		File f = null;
		try 
		{
			f = new File(url.toURI());
			if (f != null)
			{
				for (String uris : f.list())
				{
					if (uris.endsWith(".txt"))
					{
						addExperiment(uris, max_length);
					}
				}
			}
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
	}
	
	protected void addExperiment(String filename, int max_length)
	{
		InputStream is = FileHelper.internalFileToStream(AutomataLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		FilePathCountExperiment exp = new FilePathCountExperiment(scanner, max_length);
		scanner.close();
		Graph g = exp.getGraph();
		String[] vertices = new String[g.m_vertices.size()];
		int i = 0;
		for (Vertex v : g.m_vertices)
		{
			vertices[i++] = v.m_label;
		}
		ExperimentTable table = new ExperimentTable();
		table.setSeriesNames(vertices).useForX("length").useForY(null).setTitle("Number of paths for each final state in the property " + exp.readString("property"));
		add(exp, table);
		add(table);
		Table norm_tab = new NormalizeRows(table);
		add(norm_tab);
		Scatterplot plot = new Scatterplot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.withLines();
		/*BarPlot plot = new BarPlot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.rowStacked();*/
		add(plot);
	}
}
