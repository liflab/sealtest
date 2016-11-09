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
package ca.uqac.lif.ecp.atomic;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.CayleyGraph.IsomorphismException;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathList;
import static ca.uqac.lif.ecp.atomic.TestSettings.loadAutomaton;
import static org.junit.Assert.*;

/**
 * Unit tests for shallow history triaging functions
 * @author Sylvain Hallé
 */
public class ShallowHistoryTest
{
	@Test
	public void testStateHistory1() throws IsomorphismException
	{
		// The Cayley graph for state shallow history of depth 1 is the original graph itself
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<Integer>> expected = getStateGraph("test1-ssh-1.dot");
		StateShallowHistory shs = new StateShallowHistory(aut, 1);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	@Test
	public void testStateHistory2() throws IsomorphismException
	{
		// The Cayley graph for state shallow history of depth 1 is the original graph itself
		Automaton aut = loadAutomaton("test2.dot");
		Automaton expected = loadAutomaton("test2.dot");
		StateShallowHistory shs = new StateShallowHistory(aut, 1);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(aut.isIsomorphicToThrowable(expected));
	}
	
	public static CayleyGraph<AtomicEvent,MathList<Integer>> getStateGraph(String filename)
	{
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = new CayleyGraph<AtomicEvent,MathList<Integer>>();
		Scanner scanner = new Scanner(ShallowHistoryTest.class.getResourceAsStream(filename));
		Pattern edge_pat = Pattern.compile("(\\d+) -> (\\d+) \\[label=\"(.*?)\"\\];");
		
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				Matcher edge_mat = edge_pat.matcher(line);
				if (edge_mat.find())
				{
					// Edge line
					int source_id = Integer.parseInt(edge_mat.group(1));
					int dest_id = Integer.parseInt(edge_mat.group(2));
					String[] labels = edge_mat.group(3).split(",");
					for (String label : labels)
					{
						AtomicEdge edge = new AtomicEdge(source_id, new AtomicEvent(label), dest_id);
						Vertex<AtomicEvent> v = graph.getVertex(source_id);
						v.add(edge);
					}
				}
				else
				{
					// Vertex line
				}
			}
		}
		scanner.close();
		return graph;
	}
}
