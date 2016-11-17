package ca.uqac.lif.ecp.ltl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class OperatorTest
{

	@Test
	public void test1()
	{
		AtomicEvent p = new AtomicEvent("p");
		AtomicEvent q = new AtomicEvent("q");
		AtomicEvent r = new AtomicEvent("r");
		Value v;
		Operator<AtomicEvent> o = getFormula1();
		o.evaluate(p);
		v = o.getValue();
		assertEquals(Value.INCONCLUSIVE, v);
		o.evaluate(r);
		v = o.getValue();
		assertEquals(Value.INCONCLUSIVE, v);
		o.evaluate(p);
		
		
		Operator<AtomicEvent> tree = o.copy(true);
		/*for (Operator<AtomicEvent> c : tree.getTreeChildren())
		{
			c.delete();
			break;
		}*/
		PolarityDeletion<AtomicEvent> trans = new PolarityDeletion<AtomicEvent>();
		tree = trans.transform(tree);
		String dot = render(tree);
		writeToFile("thetree.dot", dot);
		v = o.getValue();
		assertEquals(v, Value.FALSE);
		//System.out.println(o);
	}
	
	// G (p | X q)
	public static Operator<AtomicEvent> getFormula1()
	{
		Operator<AtomicEvent> o = new Globally<AtomicEvent>(
				new Or<AtomicEvent>(
						new Atom<AtomicEvent>(new AtomicEvent("p")),
						new Next<AtomicEvent>(new Atom<AtomicEvent>(new AtomicEvent("q")))
				));
		return o;
	}
	
	public static String render(Operator<AtomicEvent> tree)
	{
		GraphvizHologramRenderer<AtomicEvent> renderer = new GraphvizHologramRenderer<AtomicEvent>();
		tree.acceptPrefix(renderer, true);
		String dot = renderer.toDot();
		return dot;
	}
	
	public static void writeToFile(String filename, String contents)
	{
		try 
		{
			FileOutputStream fos = new FileOutputStream(new File(filename));
			fos.write(contents.getBytes());
			fos.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
