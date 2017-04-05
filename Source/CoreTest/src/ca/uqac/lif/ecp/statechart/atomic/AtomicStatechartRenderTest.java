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
import ca.uqac.lif.ecp.statechart.NestedState;
import ca.uqac.lif.ecp.statechart.State;
import ca.uqac.lif.ecp.statechart.Statechart;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStatechartBuilder;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStatechartRenderer;

public class AtomicStatechartRenderTest
{
	/**
	 * Whether to print the contents of the graph to the console
	 */
	public static final boolean s_printToConsole = true;
	
	@Test
	public void test1() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-1.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		String dot_contents = AtomicStatechartRenderer.toDot(sc);
		assertNotNull(dot_contents);
		assertFalse(dot_contents.isEmpty());
		if (s_printToConsole)
		{
			System.out.println(dot_contents);
		}
	}
	
	@Test
	public void test2() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-2.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		String dot_contents = AtomicStatechartRenderer.toDot(sc);
		assertNotNull(dot_contents);
		assertFalse(dot_contents.isEmpty());
		if (s_printToConsole)
		{
			System.out.println(dot_contents);
		}
	}
	
	@Test
	public void test3() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-3.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		String dot_contents = AtomicStatechartRenderer.toDot(sc);
		assertNotNull(dot_contents);
		assertFalse(dot_contents.isEmpty());
		if (s_printToConsole)
		{
			System.out.println(dot_contents);
		}
	}
	
	@Test
	public void test4() throws ParseException
	{
		AtomicStatechart sc = new AtomicStatechart("Top");
		NestedState<AtomicEvent> ns = new NestedState<AtomicEvent>("NS");
		AtomicStatechart inside_1 = new AtomicStatechart();
		{
			{				
				NestedState<AtomicEvent> ns_in = new NestedState<AtomicEvent>("In1");
				AtomicStatechart inside_2 = new AtomicStatechart();
				inside_2.add(new State<AtomicEvent>("A"));
				inside_2.add(new State<AtomicEvent>("B"));
				ns_in.addStatechart(inside_2);
				inside_1.add(ns_in);				
			}
			{
				NestedState<AtomicEvent> ns_in = new NestedState<AtomicEvent>("In2");
				AtomicStatechart inside_2 = new AtomicStatechart("");
				inside_2.add(new State<AtomicEvent>("C"));
				inside_2.add(new State<AtomicEvent>("D"));
				ns_in.addStatechart(inside_2);
				inside_1.add(ns_in);
			}
			inside_1.add("In1", new AtomicTransition(new AtomicEvent("foo"), new Configuration<AtomicEvent>("In2")));
			inside_1.add("In2", new AtomicTransition(new AtomicEvent("foo"), new Configuration<AtomicEvent>("In1")));
		}
		ns.addStatechart(inside_1);
		sc.add(ns);
		String dot_contents = AtomicStatechartRenderer.toDot(sc);
		assertNotNull(dot_contents);
		assertFalse(dot_contents.isEmpty());
		if (s_printToConsole)
		{
			System.out.println(dot_contents);
		}
	}
}
