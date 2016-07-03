package ca.uqac.lif.ecp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

import ca.uqac.lif.parkbench.FileHelper;

public class CayleyTest 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		//InputStream is = FileHelper.internalFileToStream(CayleyTest.class, "lab/data/dwyer-11.txt");
		InputStream is = FileHelper.internalFileToStream(CayleyTest.class, "lab/data/aUb.dot");
		Automaton aut = Automaton.parseDot(new Scanner(is));
		AutomatonCayleyGraphFactory<MathSet<Collection<Integer>>> factory = new AutomatonCayleyGraphFactory<MathSet<Collection<Integer>>>(aut.getAlphabet());
		StateSetHistory function = new StateSetHistory(aut, 3, true);
		CayleyGraph<AtomicEvent,MathSet<Collection<Integer>>> graph = factory.getGraph(function);
		String dot_contents = graph.toDot();
		FileHelper.writeFromString(new File("cayley-out.dot"), dot_contents);
		System.out.printf("Edges: %d, Vertices: %d\n", graph.getEdgeCount(), graph.getVertexCount());
	}
}
