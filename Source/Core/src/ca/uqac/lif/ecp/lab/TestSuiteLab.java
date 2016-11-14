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
import java.util.Set;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.parkbench.CliParser;
import ca.uqac.lif.parkbench.CliParser.Argument;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.Group;
import ca.uqac.lif.parkbench.WriteInExperimentBuilder;
import ca.uqac.lif.parkbench.WriteInExperimentBuilder.ParseException;
import ca.uqac.lif.parkbench.plot.BarPlot;
import ca.uqac.lif.parkbench.plot.Scatterplot;
import ca.uqac.lif.parkbench.table.ExperimentTable;
import ca.uqac.lif.parkbench.table.NormalizeRows;
import ca.uqac.lif.parkbench.table.Table;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.structures.MathList;

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
		setTitle("Test sequence generation");

		// Experiment groups
		Group state_t_way_group = newGroup("State history");
		Group transition_t_way_group = newGroup("Transition history");

		// Setup the live experiments
		{
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
								addCayleyTransitionHistoryExperiment(uris, t, transition_t_way_group);
							}
						}
					}
				}
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
		}
		// Setup the write-in experiments
		{
			URL url = TestSuiteLab.class.getResource(s_dataPath + "related/");
			File f = null;
			WriteInExperimentBuilder<TestSuiteWriteInExperiment> builder = new WriteInExperimentBuilder<TestSuiteWriteInExperiment>();
			try 
			{
				f = new File(url.toURI());
				if (f != null)
				{
					for (String uris : f.list())
					{
						if (uris.endsWith(".csv"))
						{
							InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + "related/" + uris);
							Scanner scanner = new Scanner(is);
							Set<TestSuiteWriteInExperiment> experiments = builder.buildExperiment(new TestSuiteWriteInExperiment(), scanner);
							for (TestSuiteWriteInExperiment wie : experiments)
							{
								add(wie);
								if (wie.readString(CombinatorialTriagingFunctionProvider.FUNCTION).compareTo("State history") == 0)
								{
									state_t_way_group.add(wie);
								}	
								if (wie.readString(CombinatorialTriagingFunctionProvider.FUNCTION).compareTo("Transition history") == 0)
								{
									transition_t_way_group.add(wie);
								}
							}
						}
					}
				}
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
		}
	}

	protected void addCayleyStateHistoryExperiment(String filename, int strength, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		AutomatonProvider ap = new AutomatonParser(scanner);
		TriagingFunctionProvider<AtomicEvent,MathList<Integer>> fp = new StateHistoryProvider(ap, strength);
		CayleyGraphProvider<AtomicEvent,MathList<Integer>> gp = new TriagingFunctionCayleyGraphProvider<AtomicEvent,MathList<Integer>>(fp);
		CayleyTraceGeneratorProvider<AtomicEvent,MathList<Integer>> ctgp = new CayleyClassCoverageGenerator<AtomicEvent,MathList<Integer>>(gp);
		TestSuiteProvider<AtomicEvent> tp = new CayleyTestSuiteGenerator<AtomicEvent,MathList<Integer>>(ctgp);
		TestSuiteGenerationExperiment exp = new LiveGenerationExperiment(tp);
		scanner.close();
		add(exp);
		group.add(exp);
	}
	
	protected void addCayleyTransitionHistoryExperiment(String filename, int strength, Group group)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_dataPath + filename);
		Scanner scanner = new Scanner(is);
		AutomatonProvider ap = new AutomatonParser(scanner);
		TriagingFunctionProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> fp = new TransitionHistoryProvider(ap, strength);
		CayleyGraphProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> gp = new TriagingFunctionCayleyGraphProvider<AtomicEvent,MathList<Edge<AtomicEvent>>>(fp);
		CayleyTraceGeneratorProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> ctgp = new CayleyClassCoverageGenerator<AtomicEvent,MathList<Edge<AtomicEvent>>>(gp);
		TestSuiteProvider<AtomicEvent> tp = new CayleyTestSuiteGenerator<AtomicEvent,MathList<Edge<AtomicEvent>>>(ctgp);
		TestSuiteGenerationExperiment exp = new LiveGenerationExperiment(tp);
		scanner.close();
		add(exp);
		group.add(exp);
	}
}
