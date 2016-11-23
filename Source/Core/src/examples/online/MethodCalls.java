/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

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
package examples.online;

import java.util.Scanner;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.SpanningTreeTraceGenerator;
import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.ecp.online.PrintHook;
import ca.uqac.lif.ecp.online.TestDriver;
import ca.uqac.lif.ecp.online.TestHook;
import ca.uqac.lif.ecp.online.UnidirectionalTestDriver;
import ca.uqac.lif.structures.MathList;

/**
 * Create a test hook that converts events into method calls on an object.
 * This example is similar to {@link SimplePrint}, but we use a different
 * test hook.
 *  
 * @author Sylvain Hallé
 */
public class MethodCalls
{
	public static void main(String[] args)
	{
		// Read an automaton
		Automaton a = Automaton.parseDot(new Scanner(MethodCalls.class.getResourceAsStream("../microwave.dot")));
		// Create a test suite using state coverage
		StateShallowHistory function = new StateShallowHistory(a);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = function.getCayleyGraph();
		SpanningTreeTraceGenerator<AtomicEvent,MathList<Integer>> generator = new SpanningTreeTraceGenerator<AtomicEvent,MathList<Integer>>(graph);
		TestSuite<AtomicEvent> suite = generator.generateTraces();
		// Create a test driver
		UnidirectionalTestDriver<AtomicEvent,Object> driver = new UnidirectionalTestDriver<AtomicEvent,Object>();
		driver.setTestSuite(suite);
		// Connect the driver to the microwave hook
		//MicrowaveHook hook = new MicrowaveHook();
		PrintHook<AtomicEvent> hook = new PrintHook<AtomicEvent>();
		driver.setHook(hook);
		// Start the driver
		driver.run();
	}

	public static class MicrowaveHook implements TestHook<AtomicEvent,Object>
	{
		Microwave m_oven = new Microwave();

		@Override
		public Object execute(AtomicEvent event)
		{
			String event_name = event.getLabel();
			if (event_name.compareTo("start") == 0)
			{
				m_oven.start();
			}
			else if (event_name.contains("stop"))
			{
				m_oven.stop();
			}
			else if (event_name.compareTo("place food") == 0)
			{
				m_oven.setFood(true);
			}
			else if (event_name.compareTo("remove food") == 0)
			{
				m_oven.stop();
			}
			else if (event_name.compareTo("open door") == 0)
			{
				m_oven.open();
			}
			else if (event_name.compareTo("close door") == 0)
			{
				m_oven.close();
			}
			return null;
		}

		@Override
		public void reset() 
		{
			m_oven = new Microwave();
		}

		@Override
		public void setDriver(TestDriver<AtomicEvent, Object> driver) 
		{
			// Do nothing
		}

	}

	public static class Microwave
	{
		public boolean m_heating = false;
		public boolean m_empty = true;
		public boolean m_doorOpen = false;

		public Microwave()
		{
			super();
			System.out.println("New microwave " + this);
		}

		public void start()
		{
			m_heating = true;
			System.out.println("Heating");
		}

		public void open()
		{
			m_doorOpen = true;
			System.out.println("Opening the door");
		}

		public void close()
		{
			m_doorOpen = false;
			System.out.println("Closing the door");
		}

		public void stop()
		{
			m_heating = false;
			System.out.println("Stop heating");
		}

		public void setFood(boolean b)
		{
			m_empty = !b;
			if (b)
			{
				System.out.println("Putting food");
			}
			else
			{
				System.out.println("Removing food");
			}
		}
	}
}
