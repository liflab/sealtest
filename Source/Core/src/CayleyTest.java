

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.BoundedRandomTraceGenerator;
import ca.uqac.lif.ecp.CayleyEquivalenceCoverage;
import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.GraphPlotter;
import ca.uqac.lif.ecp.MathSet;
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
		AtomicBoundedRandomGenerator rtg = new AtomicBoundedRandomGenerator(new Random(), 10, 10, aut.getAlphabet());
		Set<Trace<AtomicEvent>> set = rtg.generateTraces();
		// Get Cayley Graph and plot
		CayleyGraph<AtomicEvent,?> graph = edgeHistory(aut, set);
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
		CayleyEquivalenceCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>> coverage = new CayleyEquivalenceCoverage<AtomicEvent,MathSet<Collection<Edge<AtomicEvent>>>>(graph, function);
		System.out.printf("Coverage: %f\n", coverage.getCoverage(set));
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
