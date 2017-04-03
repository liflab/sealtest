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
package ca.uqac.lif.ecp.statechart.atomic;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.statechart.Configuration;
import ca.uqac.lif.ecp.statechart.Statechart;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStatechartBuilder;

public class AtomicStatechartBuilderTest
{
	@Test
	public void test1() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-1.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof Statechart);
		Configuration<AtomicEvent> state = sc.getCurrentConfiguration();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getCurrentConfiguration();
		assertEquals("S1", state.getName());
	}
	
	@Test
	public void test2() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-2.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof Statechart);
		Configuration<AtomicEvent> state = sc.getCurrentConfiguration();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getCurrentConfiguration();
		assertEquals(1, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		sc.takeTransition(new AtomicEvent("c"));
		state = sc.getCurrentConfiguration();
		assertEquals(1, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J1", state.getChildren().get(0).getName());
		sc.takeTransition(new AtomicEvent("b"));
		state = sc.getCurrentConfiguration();
		assertEquals(0, state.getChildren().size());
		assertEquals("S0", state.getName());
	}
	
	@Test
	public void test3() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-3.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		assertNotNull(sc);
		assertTrue(sc instanceof Statechart);
		Configuration<AtomicEvent> state = sc.getCurrentConfiguration();
		assertEquals("S0", state.getName());
		sc.takeTransition(new AtomicEvent("a"));
		state = sc.getCurrentConfiguration();
		assertEquals(2, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		assertEquals("I0", state.getChildren().get(1).getName());
		sc.takeTransition(new AtomicEvent("d"));
		state = sc.getCurrentConfiguration();
		assertEquals(2, state.getChildren().size());
		assertEquals("S1", state.getName());
		assertEquals("J0", state.getChildren().get(0).getName());
		assertEquals("I1", state.getChildren().get(1).getName());
		sc.takeTransition(new AtomicEvent("b"));
		state = sc.getCurrentConfiguration();
		assertEquals(0, state.getChildren().size());
		assertEquals("S0", state.getName());
	}
}
