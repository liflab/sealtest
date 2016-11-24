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
package examples.coverage;

import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyCoverageRadius;
import ca.uqac.lif.ecp.CayleyCoverageRadius.RadiusMap;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.structures.MathList;

/**
 * Display the coverage radius graph from a test suite.
 * 
 * @author Sylvain Hallé
 */
public class CoverageRadius
{
	public static void main(String[] args)
	{
		// Read automaton from a file
		Automaton automaton = Automaton.parseDot(new Scanner(CoverageRadius.class.getResourceAsStream("../editor-clipboard.dot")), "Editor clipboard");
		// Create a triaging function
		StateShallowHistory function = new StateShallowHistory(automaton, 2);
		// Create a coverage radius
		CayleyCoverageRadius<AtomicEvent,MathList<Integer>> ccr = new CayleyCoverageRadius<AtomicEvent,MathList<Integer>>(function.getCayleyGraph(), function);
		ccr.setWeighted(true);
		// Read a test suite from a file
		Set<Trace<AtomicEvent>> test_suite = AtomicEvent.readTestSuite(new Scanner(CoverageRadius.class.getResourceAsStream("clipboard-test-suite-1.txt")));
		// Get the radius map from this test suite
		RadiusMap map = ccr.getCoverage(test_suite);
		// Print the Gnuplot version of this map
		System.out.println(map.toGnuplot());
	}
}
