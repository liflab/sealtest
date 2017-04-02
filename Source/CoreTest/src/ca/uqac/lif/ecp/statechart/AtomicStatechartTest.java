/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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
package ca.uqac.lif.ecp.statechart;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.Constant;
import ca.uqac.lif.ecp.ltl.LessThan;
import ca.uqac.lif.ecp.statechart.ActionException.ValueOutOfBoundsException;
import ca.uqac.lif.ecp.statechart.ActionException.VariableNotFoundException;

public class AtomicStatechartTest 
{
	static final AtomicEvent A = new AtomicEvent("a");
	static final AtomicEvent B = new AtomicEvent("b");
	static final AtomicEvent C = new AtomicEvent("c");
	static final AtomicEvent D = new AtomicEvent("d");
	
	@Test
	public void testStatechart1()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.add(new State<AtomicEvent>("S1"));
		as.add("S0", new AtomicTransition(A, new Configuration<AtomicEvent>("S1")));
		Transition<AtomicEvent> b;
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S0", list.getName());
		b = as.takeTransition(A);
		assertNotNull(b);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals("S1", list.getName());
		b = as.takeTransition(A);
		assertTrue(b instanceof TrashTransition);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals(Statechart.TRASH, list.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGroup1()
	{
		Statechart<AtomicEvent> inside = new Statechart<AtomicEvent>();
		inside.add(new State<AtomicEvent>("I0"));
		inside.add(new State<AtomicEvent>("I1"));
		inside.add("I0", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside.add("I0", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
		inside.add("I1", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside.add("I1", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
		NestedState<AtomicEvent> bs = new NestedState<AtomicEvent>("S1", inside);
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.add(bs);
		as.add("S0", new AtomicTransition(A, new Configuration<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(C, new Configuration<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		Configuration<AtomicEvent> list;
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
		assertTrue(b instanceof TrashTransition);
		list = as.getFullState();
		assertEquals(0, list.getChildren().size());
		assertEquals(Statechart.TRASH, list.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUp1()
	{
		Statechart<AtomicEvent> inside1 = new Statechart<AtomicEvent>();
		inside1.add(new State<AtomicEvent>("I0"));
		inside1.add(new State<AtomicEvent>("I1"));
		inside1.add("I0", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside1.add("I0", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
		inside1.add("I1", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside1.add("I1", new AtomicTransition(B, new Configuration.UpStateNode<AtomicEvent>(new Configuration<AtomicEvent>("S0", new Configuration<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs1 = new NestedState<AtomicEvent>("S1", inside1);
		Statechart<AtomicEvent> inside2 = new Statechart<AtomicEvent>();
		inside2.add(new State<AtomicEvent>("J0"));
		inside2.add(new State<AtomicEvent>("J1"));
		inside2.add("J0", new AtomicTransition(A, new Configuration<AtomicEvent>("J1")));
		inside2.add("J0", new AtomicTransition(B, new Configuration<AtomicEvent>("J0")));
		inside2.add("J1", new AtomicTransition(A, new Configuration<AtomicEvent>("J1")));
		inside2.add("J1", new AtomicTransition(B, new Configuration.UpStateNode<AtomicEvent>(new Configuration<AtomicEvent>("S0", new Configuration<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs2 = new NestedState<AtomicEvent>("S0", inside2);
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(bs2);
		as.add(bs1);
		as.add("S0", new AtomicTransition(C, new Configuration<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(D, new Configuration<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		Configuration<AtomicEvent> list;
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
		Statechart<AtomicEvent> inside1 = new Statechart<AtomicEvent>();
		inside1.add(new State<AtomicEvent>("I0"));
		inside1.add(new State<AtomicEvent>("I1"));
		inside1.add("I0", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside1.add("I0", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
		inside1.add("I1", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
		inside1.add("I1", new AtomicTransition(B, new Configuration.UpStateNode<AtomicEvent>(new Configuration<AtomicEvent>("S0"))));
		NestedState<AtomicEvent> bs1 = new NestedState<AtomicEvent>("S1", inside1);
		Statechart<AtomicEvent> inside2 = new Statechart<AtomicEvent>();
		inside2.add(new State<AtomicEvent>("J0"));
		inside2.add(new State<AtomicEvent>("J1"));
		inside2.add("J0", new AtomicTransition(A, new Configuration<AtomicEvent>("J1")));
		inside2.add("J0", new AtomicTransition(B, new Configuration<AtomicEvent>("J0")));
		inside2.add("J1", new AtomicTransition(A, new Configuration<AtomicEvent>("J1")));
		inside2.add("J1", new AtomicTransition(B, new Configuration.UpStateNode<AtomicEvent>(new Configuration<AtomicEvent>("S0", new Configuration<AtomicEvent>("J1")))));
		NestedState<AtomicEvent> bs2 = new NestedState<AtomicEvent>("S0", inside2);
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(bs2);
		as.add(bs1);
		as.add("S0", new AtomicTransition(C, new Configuration<AtomicEvent>("S1")));
		as.add("S1", new AtomicTransition(D, new Configuration<AtomicEvent>("S0")));
		Transition<AtomicEvent> b;
		Configuration<AtomicEvent> list;
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
			Statechart<AtomicEvent> inside = new Statechart<AtomicEvent>();
			inside.add(new State<AtomicEvent>("I0"));
			inside.add(new State<AtomicEvent>("I1"));
			inside.add("I0", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
			inside.add("I0", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
			inside.add("I1", new AtomicTransition(A, new Configuration<AtomicEvent>("I1")));
			inside.add("I1", new AtomicTransition(B, new Configuration<AtomicEvent>("I0")));
			ns.addStatechart(inside);
		}
		{
			Statechart<AtomicEvent> inside = new Statechart<AtomicEvent>();
			inside.add(new State<AtomicEvent>("J0"));
			inside.add(new State<AtomicEvent>("J1"));
			inside.add("J0", new AtomicTransition(C, new Configuration<AtomicEvent>("J1")));
			inside.add("J0", new AtomicTransition(D, new Configuration<AtomicEvent>("J0")));
			inside.add("J1", new AtomicTransition(C, new Configuration<AtomicEvent>("J1")));
			inside.add("J1", new AtomicTransition(D, new Configuration<AtomicEvent>("J0")));
			ns.addStatechart(inside);
		}
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(ns);
		Configuration<AtomicEvent> list;
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
	
	@Test
	public void testVariables1()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.setVariable("n", 0);
		{
			AtomicTransition t = new AtomicTransition(A, new Configuration<AtomicEvent>("S0"));
			t.setAction(new IncrementVariableBy<AtomicEvent>("n", 1));
			as.add("S0", t);
		}
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(0, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(1, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(2, list.m_variables.get("n"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testVariables2()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.setVariable("n", 0);
		{
			AtomicTransition t = new AtomicTransition(A, new Configuration<AtomicEvent>("S0"));
			t.setAction(new IncrementVariableBy<AtomicEvent>("n", 1));
			as.add("S0", t);
		}
		AtomicStatechart inside = new AtomicStatechart();
		{
			inside.setVariable("m", 10);
			inside.add(new State<AtomicEvent>("I0"));
			inside.add(new State<AtomicEvent>("I1"));
			AtomicTransition t = new AtomicTransition(B, new Configuration<AtomicEvent>("I1"));
			t.setAction(new ActionChain<AtomicEvent>(
					new IncrementVariableBy<AtomicEvent>("n", 2),
					new IncrementVariableBy<AtomicEvent>("m", 5)));
			inside.add("I0", t);
			inside.add("I1", new AtomicTransition(A, new Configuration.UpStateNode<AtomicEvent>(new Configuration<AtomicEvent>("S0"))));
			as.add(new NestedState<AtomicEvent>("S1", inside));
		}
		as.add("S0", new AtomicTransition(B, new Configuration<AtomicEvent>("S1")));
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(0, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(1, list.m_variables.get("n"));
		as.takeTransition(B);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(1, list.m_variables.get("n"));
		assertTrue(list.getChildren().get(0).m_variables.containsKey("m"));
		assertEquals(10, list.getChildren().get(0).m_variables.get("m"));
		as.takeTransition(B);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I1", list.getChildren().get(0).getName());
		assertEquals(3, list.m_variables.get("n"));
		assertEquals(15, list.getChildren().get(0).m_variables.get("m"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertEquals(3, list.m_variables.get("n"));
		as.takeTransition(B);
		list = as.getFullState();
		assertEquals("S1", list.getName());
		assertEquals("I0", list.getChildren().get(0).getName());
		assertEquals(3, list.m_variables.get("n"));
		assertEquals(10, list.getChildren().get(0).m_variables.get("m"));
	}
	
	@Test
	public void testGuard1()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.setVariable("n", 0);
		{
			AtomicTransition t = new AtomicTransition(A, new Configuration<AtomicEvent>("S0"));
			t.setGuard(new LessThan<AtomicEvent>(new StatechartVariableAtom<AtomicEvent,Number>("n", as), new Constant<AtomicEvent,Number>(1)));
			t.setAction(new IncrementVariableBy<AtomicEvent>("n", 1));
			as.add("S0", t);
		}
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(0, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(1, list.m_variables.get("n"));
		Transition<AtomicEvent> b = as.takeTransition(A);
		assertTrue(b instanceof TrashTransition);
	}
	
	@Test
	public void testBounds1()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		as.setVariable("n", 0);
		{
			AtomicTransition t = new AtomicTransition(A, new Configuration<AtomicEvent>("S0"));
			t.setAction(new IncrementVariableBy<AtomicEvent>("n", 1, 2));
			as.add("S0", t);
		}
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(0, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(1, list.m_variables.get("n"));
		as.takeTransition(A);
		list = as.getFullState();
		assertEquals("S0", list.getName());
		assertTrue(list.m_variables.containsKey("n"));
		assertEquals(2, list.m_variables.get("n"));
		Transition<AtomicEvent> b = as.takeTransition(A);
		// The action fails
		assertTrue(b instanceof TrashTransition);
		TrashTransition<AtomicEvent> tt = (TrashTransition<AtomicEvent>) b;
		assertTrue(tt.getException() instanceof ValueOutOfBoundsException);
	}
	
	@Test
	public void testUndefinedVariable()
	{
		Statechart<AtomicEvent> as = new Statechart<AtomicEvent>();
		as.add(new State<AtomicEvent>("S0"));
		{
			AtomicTransition t = new AtomicTransition(A, new Configuration<AtomicEvent>("S0"));
			t.setAction(new IncrementVariableBy<AtomicEvent>("n", 1, 2));
			as.add("S0", t);
		}
		Configuration<AtomicEvent> list;
		list = as.getFullState();
		assertEquals("S0", list.getName());
		Transition<AtomicEvent> b = as.takeTransition(A);
		assertTrue(b instanceof TrashTransition);
		TrashTransition<AtomicEvent> tt = (TrashTransition<AtomicEvent>) b;
		assertTrue(tt.getException() instanceof VariableNotFoundException);
	}
}
