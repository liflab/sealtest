/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hall�

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp.lab;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.graphs.Vertex;
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

public class TestSuiteLab extends Laboratory
{
	protected static transient final String s_dataPath = "data/";

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		initialize(args, TestSuiteLab.class);
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
		Group state_t_way_group = new Group("State shallow history");
		Group action_t_way_group = new Group("Action shallow history");
		
		// Setup the experiments
		URL url = TestSuiteLab.class.getResource(s_dataPath);
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
						for (int t = 1; t <= 3; t++)
						{
							addCayleyStateHistoryExperiment(uris, t, state_t_way_group);
							addCayleyActionHistoryExperiment(uris, t, action_t_way_group);
						}
						//addPathCountExperiment(uris, max_length, length_group);
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
		add(state_t_way_group);
		add(action_t_way_group);
	}
	
	protected void addPathCountExperiment(String filename, int max_length, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		PathCountExperiment exp = new PathCountExperimentMatrix(scanner, max_length);
		scanner.close();
		Automaton g = exp.getAutomaton();
		String[] vertices = new String[g.getVertexCount()];
		int i = 0;
		for (Vertex<AtomicEvent> v : g.getVertices())
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
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		StateDistributionExperiment exp = new StateDistributionExperiment(scanner, iterations);
		scanner.close();
		Automaton g = exp.getAutomaton();
		String[] vertices = new String[g.getVertexCount()];
		int i = 0;
		for (Vertex<AtomicEvent> v : g.getVertices())
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
	
	protected void addCayleyStateHistoryExperiment(String filename, int strength, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		CayleyStateShallowHistoryExperiment exp = new CayleyStateShallowHistoryExperiment();
		exp.setStrength(strength);
		CayleyAutomatonExperiment.fillExperiment(exp, scanner);
		scanner.close();
		add(exp);
		group.add(exp);
	}
	
	protected void addCayleyActionHistoryExperiment(String filename, int strength, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		CayleyActionShallowHistoryExperiment exp = new CayleyActionShallowHistoryExperiment();
		exp.setStrength(strength);
		CayleyAutomatonExperiment.fillExperiment(exp, scanner);
		scanner.close();
		add(exp);
		group.add(exp);
	}
}
