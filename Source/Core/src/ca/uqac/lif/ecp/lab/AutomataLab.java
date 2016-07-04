package ca.uqac.lif.ecp.lab;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import ca.uqac.lif.ecp.Vertex;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.parkbench.CliParser;
import ca.uqac.lif.parkbench.CliParser.Argument;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.Group;
import ca.uqac.lif.parkbench.plot.BarPlot;
import ca.uqac.lif.parkbench.plot.Scatterplot;
import ca.uqac.lif.parkbench.table.ExperimentTable;
import ca.uqac.lif.parkbench.table.NormalizeRows;
import ca.uqac.lif.parkbench.table.Table;
import ca.uqac.lif.parkbench.Laboratory;

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
		int max_length = 9;
		if (map.hasOption("max-length"))
		{
			max_length = Integer.parseInt(map.getOptionValue("max-length"));
		}
		// Give a name to the lab
		setTitle("Path Counting");
		
		// Experiment groups
		Group length_group = new Group("Distribution according to length");
		Group limit_group = new Group("Limit of distribution");
		
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
						addPathCountExperiment(uris, max_length, length_group);
						//addStateDistributionExperiment(uris, max_length, limit_group);
					}
				}
			}
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		add(length_group);
		add(limit_group);
	}
	
	protected void addPathCountExperiment(String filename, int max_length, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(AutomataLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		PathCountExperiment exp = new PathCountExperimentMatrix(scanner, max_length);
		scanner.close();
		Automaton g = exp.getGraph();
		String[] vertices = new String[g.m_vertices.size()];
		int i = 0;
		for (Vertex<AtomicEvent> v : g.m_vertices)
		{
			vertices[i++] = Integer.toString(v.getId());
		}
		ExperimentTable table = new ExperimentTable();
		table.setSeriesNames(vertices).useForX("length").useForY(null).setTitle("Number of paths for each final state in the property " + exp.readString("property"));
		add(exp, table);
		add(table);
		group.add(exp);
		Table norm_tab = new NormalizeRows(table);
		add(norm_tab);
		Scatterplot plot = new Scatterplot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.withLines();
		plot.withPoints(false);
		/*BarPlot plot = new BarPlot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.rowStacked();*/
		add(plot);
	}
	
	protected void addStateDistributionExperiment(String filename, int iterations, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(AutomataLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		StateDistributionExperiment exp = new StateDistributionExperiment(scanner, iterations);
		scanner.close();
		Automaton g = exp.getGraph();
		String[] vertices = new String[g.m_vertices.size()];
		int i = 0;
		for (Vertex<AtomicEvent> v : g.m_vertices)
		{
			vertices[i++] = Integer.toString(v.getId());
		}
		ExperimentTable table = new ExperimentTable();
		table.useForX("state").useForY("fraction").setTitle("Proportion of paths ending in each state in the property '" + exp.readString("property") +"'");
		add(exp, table);
		add(table);
		group.add(exp);
		BarPlot plot = new BarPlot(table);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.rowStacked();
		add(plot);
	}
}
