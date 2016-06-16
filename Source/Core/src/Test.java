import ca.uqac.lif.brutecount.CountVisitor;
import ca.uqac.lif.brutecount.Edge;
import ca.uqac.lif.brutecount.Graph;
import ca.uqac.lif.brutecount.Vertex;


public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Graph g = new Graph();
		{
			Vertex v = new Vertex("0");
			v.add(new Edge("a", "1"));
			v.add(new Edge("b", "0"));
			v.add(new Edge("c", "0"));
			g.add(v);
		}
		{
			Vertex v = new Vertex("1");
			v.add(new Edge("a", "1"));
			v.add(new Edge("b", "1"));
			v.add(new Edge("c", "1"));
			g.add(v);
		}
		CountVisitor visitor = new CountVisitor();
		visitor.start(g, "0", 12);
		System.out.println(visitor);
	}
	
	

}
