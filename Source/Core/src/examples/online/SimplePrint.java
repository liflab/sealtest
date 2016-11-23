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
import ca.uqac.lif.ecp.online.UnidirectionalTestDriver;
import ca.uqac.lif.structures.MathList;

/**
 * Use a simple test hook to execute a set of test sequences.
 * <p>
 * In this example, we generate a set of test sequences, and then 
 * use a {@link ca.uqac.lif.ecp.online.TestDriver TestDriver}
 * to "play" these sequences on some system under test (SUT). The way a
 * driver interacts with the SUT is through an interface called a
 * {@link ca.uqac.lif.ecp.online.TestHook TestHook}. Every time a new
 * event is to be "played", the driver calls the hook's
 * {@link ca.uqac.lif.ecp.online.TestHook#execute(ca.uqac.lif.ecp.Event) execute()}
 * method and gives it the event. It is then up to the hook's actual
 * implementation to do something with this event, which depends on the
 * context.
 * <p>
 * In our example, we illustrate this process with a simple
 * hook that just prints the event it receives to the standard output.
 * Obviously, to test a real system, one needs to implement a hook
 * that takes the input event and acts upon the SUT in a concrete way.
 *  
 * @author Sylvain Hallé
 */
public class SimplePrint
{
	public static void main(String[] args)
	{
		// Read an automaton
		Automaton a = Automaton.parseDot(new Scanner(SimplePrint.class.getResourceAsStream("../editor-clipboard.dot")));
		// Create a test suite using state coverage
		StateShallowHistory function = new StateShallowHistory(a);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = function.getCayleyGraph();
		SpanningTreeTraceGenerator<AtomicEvent,MathList<Integer>> generator = new SpanningTreeTraceGenerator<AtomicEvent,MathList<Integer>>(graph);
		TestSuite<AtomicEvent> suite = generator.generateTraces();
		// Create a test driver
		UnidirectionalTestDriver<AtomicEvent,Object> driver = new UnidirectionalTestDriver<AtomicEvent,Object>();
		driver.setTestSuite(suite);
		// Connect the driver to a simple test hook
		PrintHook<AtomicEvent> hook = new PrintHook<AtomicEvent>();
		driver.setHook(hook);
		// Start the driver
		driver.run();
	}
}
