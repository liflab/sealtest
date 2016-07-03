package ca.uqac.lif.ecp;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special case of Cayley graph where the triaging function operates
 * over atomic events
 * 
 * @author Sylvain
 */
public class AtomicCayleyGraph extends CayleyGraph<AtomicEvent>
{
	/**
	 * Creates a graph from a dot string
	 * @param input A scanner to a dot string
	 * @return The graph
	 */
	public static AtomicCayleyGraph parseDot(Scanner scanner)
	{
		AtomicCayleyGraph g = new AtomicCayleyGraph();
		Pattern pat = Pattern.compile("(.*?)->(.*?) \\[label=\"(.*?)\"\\];");
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			line = line.trim();
			if (line.isEmpty() || line.startsWith(("#")))
				continue;
			Matcher mat = pat.matcher(line);
			if (mat.find())
			{
				String s_from = mat.group(1).trim();
				int i_from = Integer.parseInt(s_from);
				Vertex<AtomicEvent> from = g.getVertex(i_from);
				if (from == null)
				{
					from = new Vertex<AtomicEvent>(i_from);
					g.add(from);
				}
				String s_to = mat.group(2).trim();
				int i_to = Integer.parseInt(s_to);
				Vertex<AtomicEvent> to = g.getVertex(i_to);
				if (to == null)
				{
					to = new Vertex<AtomicEvent>(i_to);
					g.add(to);
				}
				String[] labels = mat.group(3).trim().split(",");
				for (String label : labels)
				{
					Edge<AtomicEvent> e = new Edge<AtomicEvent>(new AtomicEvent(label), i_to);
					from.add(e);
				}
			}
		}
		scanner.close();
		return g;
	}
}
