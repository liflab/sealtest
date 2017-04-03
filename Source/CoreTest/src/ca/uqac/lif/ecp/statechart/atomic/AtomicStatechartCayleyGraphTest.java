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
import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.statechart.Configuration;
import ca.uqac.lif.ecp.statechart.Statechart;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStateShallowHistory;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStatechartBuilder;
import ca.uqac.lif.ecp.statechart.atomic.AtomicStatechartCayleyGraphFactory;
import ca.uqac.lif.structures.MathList;

public class AtomicStatechartCayleyGraphTest 
{
	@Test
	public void test1() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-1.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>>(sc);
		AtomicStateShallowHistory function = new AtomicStateShallowHistory(sc, 1);
		CayleyGraph<AtomicEvent,MathList<Configuration<AtomicEvent>>> graph = factory.getGraph(function);
		assertNotNull(graph);
		assertEquals(3, graph.getVertexCount());
	}
	
	@Test
	public void test2() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-1.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>>(sc);
		AtomicStateShallowHistory function = new AtomicStateShallowHistory(sc, 2);
		CayleyGraph<AtomicEvent,MathList<Configuration<AtomicEvent>>> graph = factory.getGraph(function);
		assertNotNull(graph);
		assertEquals(6, graph.getVertexCount());
	}
	
	@Test
	public void test3() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-2.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>>(sc);
		AtomicStateShallowHistory function = new AtomicStateShallowHistory(sc, 1);
		CayleyGraph<AtomicEvent,MathList<Configuration<AtomicEvent>>> graph = factory.getGraph(function);
		assertNotNull(graph);
		assertEquals(5, graph.getVertexCount());
	}
	
	@Test
	public void test4() throws ParseException
	{
		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("statechart-2.txt"));
		Statechart<AtomicEvent> sc = AtomicStatechartBuilder.parseFromString(scanner);
		AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>> factory = new AtomicStatechartCayleyGraphFactory<MathList<Configuration<AtomicEvent>>>(sc);
		AtomicStateShallowHistory function = new AtomicStateShallowHistory(sc, 2);
		CayleyGraph<AtomicEvent,MathList<Configuration<AtomicEvent>>> graph = factory.getGraph(function);
		assertNotNull(graph);
		assertEquals(13, graph.getVertexCount());
	}
}
