package ca.uqac.lif.ecp.ltl;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class OperatorTest
{

	@Test
	public void test1()
	{
		// G (p & X q)
		Operator<AtomicEvent> o = new Globally<AtomicEvent>(
				new And<AtomicEvent>(
						new Atom<AtomicEvent>(new AtomicEvent("p")),
						new Next<AtomicEvent>(new Atom<AtomicEvent>(new AtomicEvent("q")))
				));
		System.out.println(o);
	}
}
