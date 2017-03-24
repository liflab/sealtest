package ca.uqac.lif.ecp.statechart;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicStatechartBuilderTest
{
	@Test
	public void test1() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-1.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof SingleStatechart);
		StateNode<AtomicEvent> state = sc.getFullState();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getFullState();
		assertEquals("S1", state.getName());
	}
	
	@Test
	public void test2() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-2.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof SingleStatechart);
		StateNode<AtomicEvent> state = sc.getFullState();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getFullState();
		assertEquals(1, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		sc.takeTransition(new AtomicEvent("c"));
		state = sc.getFullState();
		assertEquals(1, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J1", state.getChildren().get(0).getName());
		sc.takeTransition(new AtomicEvent("b"));
		state = sc.getFullState();
		assertEquals(0, state.getChildren().size());
		assertEquals("S0", state.getName());
	}
	
	@Test
	public void test3() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-3.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof SingleStatechart);
		StateNode<AtomicEvent> state = sc.getFullState();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getFullState();
		assertEquals(2, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		assertEquals("I0", state.getChildren().get(1).getName());
		sc.takeTransition(new AtomicEvent("d"));
		state = sc.getFullState();
		assertEquals(2, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		assertEquals("I1", state.getChildren().get(1).getName());
		sc.takeTransition(new AtomicEvent("b"));
		state = sc.getFullState();
		assertEquals(0, state.getChildren().size());
		assertEquals("S0", state.getName());
	}
}
