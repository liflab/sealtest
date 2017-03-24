package ca.uqac.lif.ecp.statechart;

import static org.junit.Assert.*;

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
		SingleStatechart<AtomicEvent> as = new SingleStatechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.add(new State<AtomicEvent>("S1"));
		as.add("S0", new AtomicTransition(A, new StateNode<AtomicEvent>("S1")));
		Transition<AtomicEvent> b;
		StateNode<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S0", list.getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S1", list.getName());
		b = as.takeTransition(A);
		assertNull(b);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals(Statechart.TRASH, list.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGroup1()
	{
		SingleStatechart<AtomicEvent> inside = new SingleStatechart<AtomicEvent>();
		inside.add(new State<AtomicEvent>("I0"));
		inside.add(new State<AtomicEvent>("I1"));
		inside.add("I0", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside.add("I0", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
		inside.add("I1", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside.add("I1", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
		NestedState<AtomicEvent> bs = new NestedState<AtomicEvent>("S1", inside);
		SingleStatechart<AtomicEvent> as = new SingleStatechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.add(bs);
		as.add("S0", new AtomicTransition(A, new StateNode<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(C, new StateNode<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		StateNode<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S0", list.getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I1", list.getChildren().get(0).getName());
		b = as.takeTransition(C);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S0", list.getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		b = as.takeTransition(D);
		assertNull(b);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals(Statechart.TRASH, list.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUp1()
	{
		SingleStatechart<AtomicEvent> inside1 = new SingleStatechart<AtomicEvent>();
		inside1.add(new State<AtomicEvent>("I0"));
		inside1.add(new State<AtomicEvent>("I1"));
		inside1.add("I0", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside1.add("I0", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
		inside1.add("I1", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside1.add("I1", new AtomicTransition(B, new StateNode.UpStateNode<AtomicEvent>(new StateNode<AtomicEvent>("S0", new StateNode<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs1 = new NestedState<AtomicEvent>("S1", inside1);
		SingleStatechart<AtomicEvent> inside2 = new SingleStatechart<AtomicEvent>();
		inside2.add(new State<AtomicEvent>("J0"));
		inside2.add(new State<AtomicEvent>("J1"));
		inside2.add("J0", new AtomicTransition(A, new StateNode<AtomicEvent>("J1")));
		inside2.add("J0", new AtomicTransition(B, new StateNode<AtomicEvent>("J0")));
		inside2.add("J1", new AtomicTransition(A, new StateNode<AtomicEvent>("J1")));
		inside2.add("J1", new AtomicTransition(B, new StateNode.UpStateNode<AtomicEvent>(new StateNode<AtomicEvent>("S0", new StateNode<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs2 = new NestedState<AtomicEvent>("S0", inside2);
		SingleStatechart<AtomicEvent> as = new SingleStatechart<AtomicEvent>();
		as.add(bs2);
		as.add(bs1);
		as.add("S0", new AtomicTransition(C, new StateNode<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(D, new StateNode<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		StateNode<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S0", list.getName());
		assertEquals("J0", list.getChildren().get(0).getName());
		b = as.takeTransition(C);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I1", list.getChildren().get(0).getName());
		b = as.takeTransition(B);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertEquals("J1", list.getChildren().get(0).getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUp2()
	{
		SingleStatechart<AtomicEvent> inside1 = new SingleStatechart<AtomicEvent>();
		inside1.add(new State<AtomicEvent>("I0"));
		inside1.add(new State<AtomicEvent>("I1"));
		inside1.add("I0", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside1.add("I0", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
		inside1.add("I1", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
		inside1.add("I1", new AtomicTransition(B, new StateNode.UpStateNode<AtomicEvent>(new StateNode<AtomicEvent>("S0"))));
		NestedState<AtomicEvent> bs1 = new NestedState<AtomicEvent>("S1", inside1);
		SingleStatechart<AtomicEvent> inside2 = new SingleStatechart<AtomicEvent>();
		inside2.add(new State<AtomicEvent>("J0"));
		inside2.add(new State<AtomicEvent>("J1"));
		inside2.add("J0", new AtomicTransition(A, new StateNode<AtomicEvent>("J1")));
		inside2.add("J0", new AtomicTransition(B, new StateNode<AtomicEvent>("J0")));
		inside2.add("J1", new AtomicTransition(A, new StateNode<AtomicEvent>("J1")));
		inside2.add("J1", new AtomicTransition(B, new StateNode.UpStateNode<AtomicEvent>(new StateNode<AtomicEvent>("S0", new StateNode<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs2 = new NestedState<AtomicEvent>("S0", inside2);
		SingleStatechart<AtomicEvent> as = new SingleStatechart<AtomicEvent>();
		as.add(bs2);
		as.add(bs1);
		as.add("S0", new AtomicTransition(C, new StateNode<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(D, new StateNode<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		StateNode<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S0", list.getName());
		assertEquals("J0", list.getChildren().get(0).getName());
		b = as.takeTransition(C);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(1, list.getChildren().size());
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I1", list.getChildren().get(0).getName());
		b = as.takeTransition(B);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertEquals("J0", list.getChildren().get(0).getName());
	}
	
	@Test
	public void testOrthogonal1()
	{
		NestedState<AtomicEvent> ns = new NestedState<AtomicEvent>("IN");
		{
			SingleStatechart<AtomicEvent> inside = new SingleStatechart<AtomicEvent>();
			inside.add(new State<AtomicEvent>("I0"));
			inside.add(new State<AtomicEvent>("I1"));
			inside.add("I0", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
			inside.add("I0", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
			inside.add("I1", new AtomicTransition(A, new StateNode<AtomicEvent>("I1")));
			inside.add("I1", new AtomicTransition(B, new StateNode<AtomicEvent>("I0")));
			ns.addStatechart(inside);
		}
		{
			SingleStatechart<AtomicEvent> inside = new SingleStatechart<AtomicEvent>();
			inside.add(new State<AtomicEvent>("J0"));
			inside.add(new State<AtomicEvent>("J1"));
			inside.add("J0", new AtomicTransition(C, new StateNode<AtomicEvent>("J1")));
			inside.add("J0", new AtomicTransition(D, new StateNode<AtomicEvent>("J0")));
			inside.add("J1", new AtomicTransition(C, new StateNode<AtomicEvent>("J1")));
			inside.add("J1", new AtomicTransition(D, new StateNode<AtomicEvent>("J0")));
			ns.addStatechart(inside);
		}
		SingleStatechart<AtomicEvent> as = new SingleStatechart<AtomicEvent>();
		as.add(ns);
		StateNode<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(2, list.getChildren().size());
		assertEquals("IN", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		assertEquals("J0", list.getChildren().get(1).getName());
		Transition<AtomicEvent> b = as.takeTransition(C);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(2, list.getChildren().size());
		assertEquals("IN", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		assertEquals("J1", list.getChildren().get(1).getName());
		
	}
}
