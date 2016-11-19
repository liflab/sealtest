/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

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

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.lab.fsm.AutomatonParser;
import ca.uqac.lif.ecp.lab.fsm.AutomatonProvider;
import ca.uqac.lif.ecp.lab.fsm.CombinatorialTriagingFunctionProvider;
import ca.uqac.lif.ecp.lab.fsm.StateHistoryProvider;
import ca.uqac.lif.ecp.lab.fsm.TransitionHistoryProvider;
import ca.uqac.lif.ecp.lab.ltl.HologramTransformationProvider;
import ca.uqac.lif.ecp.lab.ltl.HologramTriagingFunctionProvider;
import ca.uqac.lif.ecp.lab.ltl.OperatorProvider;
import ca.uqac.lif.ecp.lab.ltl.StringOperatorProvider;
import ca.uqac.lif.ecp.lab.ltl.StringTransformationProvider;
import ca.uqac.lif.ecp.lab.pages.FsmCallback;
import ca.uqac.lif.ecp.lab.pages.GetAutomatonCallback;
import ca.uqac.lif.ecp.lab.pages.ShowAutomatonCallback;
import ca.uqac.lif.ecp.ltl.AtomicLtlCayleyGraphFactory;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.parkbench.CliParser;
import ca.uqac.lif.parkbench.CliParser.Argument;
import ca.uqac.lif.parkbench.CliParser.ArgumentMap;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.Group;
import ca.uqac.lif.parkbench.WriteInExperimentBuilder;
import ca.uqac.lif.parkbench.WriteInExperimentBuilder.ParseException;
import ca.uqac.lif.parkbench.server.ParkBenchCallback;
import ca.uqac.lif.parkbench.table.ExperimentMultidimensionalTable;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.structures.MathList;

/**
 * Main class setting up the laboratory
 * 
 * @author Sylvain Hallé
 */
public class TestSuiteLab extends Laboratory
{
	/**
	 * The path where data will be fetched from
	 */
	public static transient final String s_dataPath = "data/";

	/**
	 * The path where write-in experiments will be fetched from
	 */
	public static transient final String s_writeInPath = s_dataPath + "related/";

	/**
	 * The path where FSMs will be read from
	 */
	public static transient final String s_fsmPath = s_dataPath + "fsm/";
	
	/**
	 * The path where LTL formulas will be read from
	 */
	public static transient final String s_ltlPath = s_dataPath + "ltl/";

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
	public void setupExperiments(ArgumentMap map, List<ParkBenchCallback> callbacks)
	{	
		int max_t = 3;
		if (map.hasOption("max-length"))
		{
			max_t = Integer.parseInt(map.getOptionValue("max-length"));
		}
		// Give a name and a description to the lab
		setTitle("Test sequence generation");
		setDescription(FileHelper.internalFileToString(this.getClass(), "pages/description.html"));
		
		// Register custom pages for the server
		registerCustomPages(callbacks);

		// Create experiment groups and tables
		Group state_t_way_group = newGroup("State history");
		Group transition_t_way_group = newGroup("Transition history");
		Group ltl_group = newGroup("LTL holograms");
		ExperimentMultidimensionalTable mt_state_coverage = new ExperimentMultidimensionalTable(new String[]{"Property", "Strength", "Method", "Size", "Length"});
		mt_state_coverage.setTitle("State coverage");
		ExperimentMultidimensionalTable mt_transition_coverage = new ExperimentMultidimensionalTable(new String[]{"Property", "Strength", "Method", "Size", "Length"});
		mt_transition_coverage.setTitle("Transition coverage");
		ExperimentMultidimensionalTable ltl_table = new ExperimentMultidimensionalTable(new String[]{"Formula", "Transformation", "Size", "Length", "Shortest", "Longest"});
		add(mt_state_coverage);
		add(mt_transition_coverage);
		add(ltl_table);
		
		/* AUTOMATA-BASED EXPERIMENTS */

		// Setup the live experiments
		{
			List<String> listing = FileHelper.listAllFiles(TestSuiteLab.class.getResource(s_fsmPath), ".*\\.txt");
			for (String uris : listing)
			{
				for (int t = 1; t <= max_t; t++)
				{
					addCayleyStateHistoryExperiment(uris, t, state_t_way_group, mt_state_coverage);
					addCayleyTransitionHistoryExperiment(uris, t, transition_t_way_group, mt_transition_coverage);
				}
			}
		}
		// Setup the write-in experiments
		{
			List<String> listing = FileHelper.listAllFiles(TestSuiteLab.class.getResource(s_writeInPath), ".*\\.csv");
			WriteInExperimentBuilder<TestSuiteWriteInExperiment> builder = new WriteInExperimentBuilder<TestSuiteWriteInExperiment>();
			for (String uris : listing)
			{
				InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_writeInPath + uris);
				Scanner scanner = new Scanner(is);
				Set<TestSuiteWriteInExperiment> experiments;
				try 
				{
					experiments = builder.buildExperiment(new TestSuiteWriteInExperiment(), scanner);
					for (TestSuiteWriteInExperiment wie : experiments)
					{
						add(wie);
						if (wie.readString(CombinatorialTriagingFunctionProvider.FUNCTION).compareTo("State history") == 0)
						{
							state_t_way_group.add(wie);
							mt_state_coverage.add(wie);
						}	
						if (wie.readString(CombinatorialTriagingFunctionProvider.FUNCTION).compareTo("Transition history") == 0)
						{
							transition_t_way_group.add(wie);
							mt_transition_coverage.add(wie);
						}
					}
				} 
				catch (ParseException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		/* LTL-BASED EXPERIMENTS */
		// Setup the live experiments
		{
			InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_ltlPath + "formulas.txt");
			Scanner scanner = new Scanner(is);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine().trim();
				if (line.isEmpty() || line.startsWith("#"))
				{
					continue;
				}
				String[] parts = line.split("\\t+");
				OperatorProvider<AtomicEvent> op = new StringOperatorProvider(parts[1]);
				HologramTransformationProvider<AtomicEvent> htp = new StringTransformationProvider(parts[2]);
				HologramTriagingFunctionProvider<AtomicEvent> htfp = new HologramTriagingFunctionProvider<AtomicEvent>(op, htp);
				CayleyGraphProvider<AtomicEvent,Operator<AtomicEvent>> gp = new FactoryCayleyGraphProvider<AtomicEvent,Operator<AtomicEvent>>(new AtomicLtlCayleyGraphFactory(), htfp);
				CayleyTraceGeneratorProvider<AtomicEvent,Operator<AtomicEvent>> ctgp = new CayleyClassCoverageGenerator<AtomicEvent,Operator<AtomicEvent>>(gp);
				TestSuiteProvider<AtomicEvent> tp = new CayleyTestSuiteGenerator<AtomicEvent,Operator<AtomicEvent>>(ctgp);
				TestSuiteGenerationExperiment exp = new LiveGenerationExperiment(tp);
				add(exp);
				ltl_table.add(exp);
				ltl_group.add(exp);
			}
			scanner.close();
		}
	}

	protected void addCayleyStateHistoryExperiment(String filename, int strength, Group group, ExperimentMultidimensionalTable table)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_fsmPath + filename);
		Scanner scanner = new Scanner(is);
		AutomatonProvider ap = new AutomatonParser(scanner);
		TriagingFunctionProvider<AtomicEvent,MathList<Integer>> fp = new StateHistoryProvider(ap, strength);
		CayleyGraphProvider<AtomicEvent,MathList<Integer>> gp = new TriagingFunctionCayleyGraphProvider<AtomicEvent,MathList<Integer>>(fp);
		CayleyTraceGeneratorProvider<AtomicEvent,MathList<Integer>> ctgp = new CayleyClassCoverageGenerator<AtomicEvent,MathList<Integer>>(gp);
		TestSuiteProvider<AtomicEvent> tp = new CayleyTestSuiteGenerator<AtomicEvent,MathList<Integer>>(ctgp);
		TestSuiteGenerationExperiment exp = new LiveGenerationExperiment(tp);
		scanner.close();
		add(exp);
		table.add(exp);
		group.add(exp);
	}

	protected void addCayleyTransitionHistoryExperiment(String filename, int strength, Group group, ExperimentMultidimensionalTable table)
	{
		InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, s_fsmPath + filename);
		Scanner scanner = new Scanner(is);
		AutomatonProvider ap = new AutomatonParser(scanner);
		TriagingFunctionProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> fp = new TransitionHistoryProvider(ap, strength);
		CayleyGraphProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> gp = new TriagingFunctionCayleyGraphProvider<AtomicEvent,MathList<Edge<AtomicEvent>>>(fp);
		CayleyTraceGeneratorProvider<AtomicEvent,MathList<Edge<AtomicEvent>>> ctgp = new CayleyClassCoverageGenerator<AtomicEvent,MathList<Edge<AtomicEvent>>>(gp);
		TestSuiteProvider<AtomicEvent> tp = new CayleyTestSuiteGenerator<AtomicEvent,MathList<Edge<AtomicEvent>>>(ctgp);
		TestSuiteGenerationExperiment exp = new LiveGenerationExperiment(tp);
		scanner.close();
		add(exp);
		table.add(exp);
		group.add(exp);
	}
	
	protected void registerCustomPages(List<ParkBenchCallback> callbacks)
	{
		callbacks.add(new FsmCallback(this, null));
		callbacks.add(new GetAutomatonCallback(this, null));
		callbacks.add(new ShowAutomatonCallback(this, null));
	}
}
