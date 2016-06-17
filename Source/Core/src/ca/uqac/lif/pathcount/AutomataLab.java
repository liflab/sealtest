package ca.uqac.lif.pathcount;
import java.io.InputStream;
import java.util.Scanner;

import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.plot.BarPlot;
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
		addExperiment("dwyer-01.txt", max_length);
		addExperiment("dwyer-02.txt", max_length);
		addExperiment("dwyer-03.txt", max_length);
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
		BarPlot plot = new BarPlot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + exp.readString("property"));
		plot.rowStacked();
		add(plot);
	}
}
