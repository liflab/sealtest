package ca.uqac.lif.pathcount;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.plot.BarPlot;
import ca.uqac.lif.parkbench.table.ExperimentTable;
import ca.uqac.lif.parkbench.table.NormalizeRows;
import ca.uqac.lif.parkbench.table.Table;
import ca.uqac.lif.parkbench.Laboratory;

public class AutomataLab extends Laboratory
{

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
		addExperiment("Fa.dot", "F a", max_length);
		addExperiment("aUb.dot", "a U b", max_length);
		addExperiment("GaXb.dot", "G (a -> X b)", max_length);
		
	}
	
	protected void addExperiment(String filename, String title, int max_length)
	{
		FilePathCountExperiment exp = new FilePathCountExperiment(title, filename, max_length);
		Graph g = exp.getGraph();
		String[] vertices = new String[g.m_vertices.size()];
		int i = 0;
		for (Vertex v : g.m_vertices)
		{
			vertices[i++] = v.m_label;
		}
		ExperimentTable table = new ExperimentTable();
		table.setSeriesNames(vertices).useForX("length").useForY(null).setTitle("Number of paths for each final state in the property " + title);
		add(exp, table);
		add(table);
		Table norm_tab = new NormalizeRows(table);
		BarPlot plot = new BarPlot(norm_tab);
		plot.labelX("Length").labelY("% of traces").setTitle("Proportion of paths for each final state in the property " + title);
		plot.rowStacked();
		add(plot);
	}
}
