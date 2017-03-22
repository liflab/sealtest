package ca.uqac.lif.ecp.statechart;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicStatechartTest 
{
	static final AtomicEvent A = new AtomicEvent("a");
	static final AtomicEvent B = new AtomicEvent("b");
	static final AtomicEvent C = new AtomicEvent("c");
	static final AtomicEvent D = new AtomicEvent("d");
	
	@Test
	public void testStatechart1()
	{
		AtomicStatechart as = new AtomicStatechart();
		as.add(new State("S0"));
		as.add(new State("S1"));
		as.add("S0", new AtomicTransition(A, "S1"));
		boolean b;
		List<String> list;
		list = as.getCurrentState();
		assertEquals(1, list.size());
		assertEquals("S0", list.get(0));
		b = as.takeTransition(A);
		assertTrue(b);
		list = as.getCurrentState();
		assertEquals(1, list.size());
		assertEquals("S1", list.get(0));
		b = as.takeTransition(A);
		assertFalse(b);
		list = as.getCurrentState();
		assertEquals(1, list.size());
		assertEquals("TRASH", list.get(0));
	}
	
	@Test
	public void testGroup1()
	{
		AtomicStatechart inside = new AtomicStatechart();
		inside.add(new State("I0"));
		inside.add(new State("I1"));
		inside.add("I0", new AtomicTransition(A, "I1"));
		inside.add("I0", new AtomicTransition(B, "I0"));
		inside.add("I1", new AtomicTransition(A, "I1"));
		inside.add("I1", new AtomicTransition(B, "I0"));
		BoxState<AtomicEvent> bs = new BoxState<AtomicEvent>("S1", inside);
		AtomicStatechart as = new AtomicStatechart();
		as.add(new State("S0"));
		as.add(bs);
		as.add("S0", new AtomicTransition(A, "S1"));
		as.add("S1", new AtomicTransition(C, "S0"));
		boolean b;
		List<String> list;
		list = as.getCurrentState();
		assertEquals(1, list.size());
		assertEquals("S0", list.get(0));
		b = as.takeTransition(A);
		assertTrue(b);
		list = as.getCurrentState();
		assertEquals(2, list.size());
		assertEquals("S1", list.get(0));
		assertEquals("I0", list.get(1));
		b = as.takeTransition(A);
		assertTrue(b);
		list = as.getCurrentState();
		assertEquals("S1", list.get(0));
		assertEquals("I1", list.get(1));
		b = as.takeTransition(C);
		assertTrue(b);
		list = as.getCurrentState();
		assertEquals(1, list.size());
		assertEquals("S0", list.get(0));
	}
}
