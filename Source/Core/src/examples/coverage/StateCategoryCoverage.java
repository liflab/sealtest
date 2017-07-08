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
package examples.coverage;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyCategoryCoverage;
import ca.uqac.lif.ecp.CayleyCoverageRadius;
import ca.uqac.lif.ecp.CayleyCoverageRadius.RadiusMap;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.structures.MathList;

/**
 * Computes category coverage from a test suite.
 * 
 * @author Sylvain Hallé
 */
public class StateCategoryCoverage
{
	public static void main(String[] args)
	{
		// Read automaton from a file
		InputStream spec_stream = StateCategoryCoverage.class.getResourceAsStream("../editor-clipboard.dot");
		Automaton automaton = Automaton.parseDot(new Scanner(spec_stream), "Editor clipboard");
		// Create a triaging function
		// Hint: try to change the second parameter, which represents history depth
		StateShallowHistory function = new StateShallowHistory(automaton, 2);
		// Create a coverage radius
		CayleyCategoryCoverage<AtomicEvent,MathList<Integer>> ccr = new CayleyCategoryCoverage<AtomicEvent,MathList<Integer>>(function.getCayleyGraph(), function);
		// Read a test suite from a file
		InputStream test_stream = StateCategoryCoverage.class.getResourceAsStream("../clipboard-test-suite-1.txt");
		Set<Trace<AtomicEvent>> test_suite = AtomicEvent.readTestSuite(new Scanner(test_stream));
		// Get the coverage metric
		float coverage = ccr.getCoverage(test_suite);
		// Print the Gnuplot version of this map
		System.out.println("Coverage of this test suite is " + coverage);
	}
}
