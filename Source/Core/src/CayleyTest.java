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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.BoundedRandomTraceGenerator;
import ca.uqac.lif.ecp.CayleyCardinalityCoverage;
import ca.uqac.lif.ecp.CayleyCategoryCoverage;
import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.CayleyDiameterCoverage;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.GraphPlotter;
import ca.uqac.lif.ecp.MathSet;
import ca.uqac.lif.ecp.SpanningTreeTraceGenerator;
import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicBoundedRandomGenerator;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.AtomicTrace;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.AutomatonCayleyGraphFactory;
import ca.uqac.lif.ecp.atomic.EdgeSetHistory;
import ca.uqac.lif.ecp.atomic.StateSetHistory;
import ca.uqac.lif.parkbench.FileHelper;

public class CayleyTest 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		// Set filenames
		String automaton_filename = "ca/uqac/lif/ecp/lab/data/simple.dot";
		String traces_filename = "ca/uqac/lif/ecp/lab/data/traces/simple.dot";
		String output_graph_filename = "cayley-out.dot";
		// Read property
		InputStream automaton_is = FileHelper.internalFileToStream(CayleyTest.class, automaton_filename);
		Automaton aut = Automaton.parseDot(new Scanner(automaton_is));
		// Read traces
		/*InputStream traces_is = FileHelper.internalFileToStream(CayleyTest.class, traces_filename);
		Set<Trace<AtomicEvent>> set = AtomicTrace.readSet(new Scanner(traces_is));*/
		// Generate traces with a generator
		//AtomicBoundedRandomGenerator rtg = new AtomicBoundedRandomGenerator(new Random(), 10, 10, aut.getAlphabet());
		//TestSuite<AtomicEvent> set = rtg.generateTraces();
		// Get Cayley Graph and plot
		//CayleyGraph<AtomicEvent,?> graph = edgeHistory(aut, set);
		CayleyGraph<AtomicEvent,?> graph = edgeHistory2(aut);
		GraphPlotter<AtomicEvent,?> plotter = graph.plotter();
		String dot_contents = plotter.toDot(GraphPlotter.Format.DOT);
		FileHelper.writeFromString(new File(output_graph_filename), dot_contents);
		System.out.printf("Edges: %d, Vertices: %d\n", graph.getEdgeCount(), graph.getVertexCount());
	}
	
	public static CayleyGraph<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> edgeHistory(Automaton aut, Set<Trace<AtomicEvent>> set)
	{
		AutomatonCayleyGraphFactory<MathSet<Collection<Edge<AtomicEvent>>>> factory = new AutomatonCayleyGraphFactory<MathSet<Collection<Edge<AtomicEvent>>>>(aut.getAlphabet());
		EdgeSetHistory function = new EdgeSetHistory(aut, 2, false, false);
		CayleyGraph<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> graph = factory.getGraph(function);
		CayleyCategoryCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> cat_coverage = new CayleyCategoryCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		CayleyCardinalityCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> card_coverage = new CayleyCardinalityCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		CayleyDiameterCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> len_coverage = new CayleyDiameterCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		System.out.printf("Category Coverage: \t%f\n", cat_coverage.getCoverage(set));
		System.out.printf("Cardinality Coverage: \t%f\n", card_coverage.getCoverage(set));
		System.out.printf("Max-Len Coverage: \t%f\n", len_coverage.getCoverage(set));
		return graph;
	}
	
	public static CayleyGraph<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> edgeHistory2(Automaton aut)
	{
		AutomatonCayleyGraphFactory<MathSet<Collection<Edge<AtomicEvent>>>> factory = new AutomatonCayleyGraphFactory<MathSet<Collection<Edge<AtomicEvent>>>>(aut.getAlphabet());
		EdgeSetHistory function = new EdgeSetHistory(aut, 2, true, false);
		CayleyGraph<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> graph = factory.getGraph(function);
		SpanningTreeTraceGenerator<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> stg = new SpanningTreeTraceGenerator<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph);
		TestSuite<AtomicEvent> set = stg.generateTraces();
		System.out.printf("%d resets, %d length\n", set.size(), set.getTotalLength());
		FileHelper.writeFromString(new File("spanning.dot"), stg.getSpanningTree().plotter().toDot());
		CayleyCategoryCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> cat_coverage = new CayleyCategoryCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		CayleyCardinalityCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> card_coverage = new CayleyCardinalityCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		CayleyDiameterCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> len_coverage = new CayleyDiameterCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		System.out.printf("Category Coverage: \t%f\n", cat_coverage.getCoverage(set));
		System.out.printf("Cardinality Coverage: \t%f\n", card_coverage.getCoverage(set));
		System.out.printf("Max-Len Coverage: \t%f\n", len_coverage.getCoverage(set));
		return graph;
	}

	
	public static CayleyGraph<AtomicEvent,MathSet<Collection<Integer>>> stateHistory(Automaton aut)
	{
		AutomatonCayleyGraphFactory<MathSet<Collection<Integer>>> factory = new AutomatonCayleyGraphFactory<MathSet<Collection<Integer>>>(aut.getAlphabet());
		StateSetHistory function = new StateSetHistory(aut, 3, true);
		CayleyGraph<AtomicEvent,MathSet<Collection<Integer>>> graph = factory.getGraph(function);
		return graph;
	}
}
