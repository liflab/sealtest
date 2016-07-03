package ca.uqac.lif.ecp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import ca.uqac.lif.ecp.StateSetHistory.History;
import ca.uqac.lif.parkbench.FileHelper;

public class CayleyTest 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		InputStream is = FileHelper.internalFileToStream(CayleyTest.class, "lab/data/aUb.dot");
		Automaton aut = Automaton.parseDot(new Scanner(is));
		AutomatonCayleyGraphFactory<History> factory = new AutomatonCayleyGraphFactory<History>(aut.getAlphabet());
		StateSetHistory function = new StateSetHistory(aut, 3);
		CayleyGraph<AtomicEvent,History> graph = factory.getGraph(function);
		String dot_contents = graph.toDot();
		FileHelper.writeFromString(new File("cayley-out.dot"), dot_contents);
		
	}
}
