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
}
