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
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathList;
import ca.uqac.lif.structures.MathSet;
import static ca.uqac.lif.ecp.atomic.TestSettings.loadAutomaton;
import static org.junit.Assert.*;

/**
 * Unit tests for shallow history triaging functions
 * @author Sylvain Hallé
 */
public class ShallowHistoryTest
{
	@Test
	public void testStateHistory1d1() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<Integer>> expected = getStateGraph("test1-ssh-1.dot");
		StateShallowHistory shs = new StateShallowHistory(aut, 1);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	@Test
	public void testStateHistory1d2() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<Integer>> expected = getStateGraph("test1-ssh-2.dot");
		StateShallowHistory shs = new StateShallowHistory(aut, 2);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}

	@Test
	public void testStateHistory2d2() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test2.dot");
		CayleyGraph<AtomicEvent,MathList<Integer>> expected = getStateGraph("test2-ssh-2.dot");
		StateShallowHistory shs = new StateShallowHistory(aut, 2);
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	@Test
	public void testTransitionHistory1d1() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> expected = getEdgeGraph("test1-tsh-1.dot");
		TransitionShallowHistory shs = new TransitionShallowHistory(aut, 1);
		CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	@Test
	public void testTransitionHistory1d2() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> expected = getEdgeGraph("test1-tsh-2.dot");
		TransitionShallowHistory shs = new TransitionShallowHistory(aut, 2);
		CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	@Test
	public void testActionHistory1d1() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test1.dot");
		CayleyGraph<AtomicEvent,MathList<AtomicEvent>> expected = getActionGraph("test1-ash-1.dot");
		ActionShallowHistory shs = new ActionShallowHistory(aut, 1);
		CayleyGraph<AtomicEvent,MathList<AtomicEvent>> graph = shs.getCayleyGraph();
		assertNotNull(graph);
		assertTrue(graph.isIsomorphicToThrowable(expected));
	}
	
	public static CayleyGraph<AtomicEvent,MathList<Integer>> getStateGraph(String filename)
	{
		CayleyGraph<AtomicEvent,MathList<Integer>> graph = new CayleyGraph<AtomicEvent,MathList<Integer>>();
		Scanner scanner = new Scanner(ShallowHistoryTest.class.getResourceAsStream(TestSettings.s_dataFolder + filename));
		Pattern edge_pat = Pattern.compile("(\\d+) -> (\\d+) \\[label=\"(.*?)\"\\];");
		Pattern vertex_pat = Pattern.compile("(\\d+) \\[label=\"(.*?)\"\\];");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				// Ignore this ling
				continue;
			}
			Matcher edge_mat = edge_pat.matcher(line);
			if (edge_mat.find())
			{
				// Edge line
				int source_id = Integer.parseInt(edge_mat.group(1));
				int dest_id = Integer.parseInt(edge_mat.group(2));
				String[] labels = edge_mat.group(3).split(",");
				Vertex<AtomicEvent> v = graph.getVertex(source_id);
				if (v == null)
				{
					v = new Vertex<AtomicEvent>(source_id);
					graph.add(v);
				}
				for (String label : labels)
				{
					AtomicEdge edge = new AtomicEdge(source_id, new AtomicEvent(label), dest_id);						
					v.add(edge);
				}
			}
			else
			{
				// Vertex line
				Matcher vertex_mat = vertex_pat.matcher(line);
				if (!vertex_mat.find())
				{
					// Ignore line
					continue;
				}
				int vertex_id = Integer.parseInt(vertex_mat.group(1));
				String s_label = vertex_mat.group(2);
				Pattern set_pat = Pattern.compile("\\[(.*?)\\]");
				Matcher set_mat = set_pat.matcher(s_label);
				MathSet<MathList<Integer>> labelling = new MathSet<MathList<Integer>>();
				while (set_mat.find())
				{
					String[] ids = set_mat.group(1).split(",");
					MathList<Integer> list = new MathList<Integer>();
					for (String s_id : ids)
					{
						list.add(Integer.parseInt(s_id.trim()));
					}
					labelling.add(list);
				}
				graph.getLabelling().put(vertex_id, labelling);
			}
		}
		scanner.close();
		return graph;
	}
	
	public static CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> getEdgeGraph(String filename)
	{
		CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>> graph = new CayleyGraph<AtomicEvent,MathList<Edge<AtomicEvent>>>();
		Scanner scanner = new Scanner(ShallowHistoryTest.class.getResourceAsStream(TestSettings.s_dataFolder + filename));
		Pattern edge_pat = Pattern.compile("(\\d+) -> (\\d+) \\[label=\"(.*?)\"\\];");
		Pattern vertex_pat = Pattern.compile("(\\d+) \\[label=\"(.*?)\"\\];");
		Pattern set_pat = Pattern.compile("\\[(.*?)\\]");
		Pattern trans_pat = Pattern.compile("(\\d+)-(.*?)->(\\d+)");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				// Ignore this ling
				continue;
			}
			Matcher edge_mat = edge_pat.matcher(line);
			if (edge_mat.find())
			{
				// Edge line
				int source_id = Integer.parseInt(edge_mat.group(1));
				int dest_id = Integer.parseInt(edge_mat.group(2));
				String[] labels = edge_mat.group(3).split(",");
				Vertex<AtomicEvent> v = graph.getVertex(source_id);
				if (v == null)
				{
					v = new Vertex<AtomicEvent>(source_id);
					graph.add(v);
				}
				for (String label : labels)
				{
					AtomicEdge edge = new AtomicEdge(source_id, new AtomicEvent(label), dest_id);						
					v.add(edge);
				}
			}
			else
			{
				// Vertex line
				Matcher vertex_mat = vertex_pat.matcher(line);
				if (!vertex_mat.find())
				{
					// Ignore line
					continue;
				}
				int vertex_id = Integer.parseInt(vertex_mat.group(1));
				String s_label = vertex_mat.group(2);
				Matcher set_mat = set_pat.matcher(s_label);
				MathSet<MathList<Edge<AtomicEvent>>> labelling = new MathSet<MathList<Edge<AtomicEvent>>>();
				while (set_mat.find())
				{
					String[] ids = set_mat.group(1).split(",");
					MathList<Edge<AtomicEvent>> list = new MathList<Edge<AtomicEvent>>();
					for (String s_edge : ids)
					{
						Matcher trans_mat = trans_pat.matcher(s_edge);
						if (trans_mat.find())
						{
							int src_id = Integer.parseInt(trans_mat.group(1));
							int dst_id = Integer.parseInt(trans_mat.group(3));
							Edge<AtomicEvent> edge = new Edge<AtomicEvent>(src_id, new AtomicEvent(trans_mat.group(2).trim()), dst_id);
							list.add(edge);
						}
					}
					labelling.add(list);
				}
				graph.getLabelling().put(vertex_id, labelling);
			}
		}
		scanner.close();
		return graph;
	}
	
	public static CayleyGraph<AtomicEvent,MathList<AtomicEvent>> getActionGraph(String filename)
	{
		CayleyGraph<AtomicEvent,MathList<AtomicEvent>> graph = new CayleyGraph<AtomicEvent,MathList<AtomicEvent>>();
		Scanner scanner = new Scanner(ShallowHistoryTest.class.getResourceAsStream(TestSettings.s_dataFolder + filename));
		Pattern edge_pat = Pattern.compile("(\\d+) -> (\\d+) \\[label=\"(.*?)\"\\];");
		Pattern vertex_pat = Pattern.compile("(\\d+) \\[label=\"(.*?)\"\\];");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				// Ignore this ling
				continue;
			}
			Matcher edge_mat = edge_pat.matcher(line);
			if (edge_mat.find())
			{
				// Edge line
				int source_id = Integer.parseInt(edge_mat.group(1));
				int dest_id = Integer.parseInt(edge_mat.group(2));
				String[] labels = edge_mat.group(3).split(",");
				Vertex<AtomicEvent> v = graph.getVertex(source_id);
				if (v == null)
				{
					v = new Vertex<AtomicEvent>(source_id);
					graph.add(v);
				}
				for (String label : labels)
				{
					AtomicEdge edge = new AtomicEdge(source_id, new AtomicEvent(label), dest_id);						
					v.add(edge);
				}
			}
			else
			{
				// Vertex line
				Matcher vertex_mat = vertex_pat.matcher(line);
				if (!vertex_mat.find())
				{
					// Ignore line
					continue;
				}
				int vertex_id = Integer.parseInt(vertex_mat.group(1));
				String s_label = vertex_mat.group(2);
				Pattern set_pat = Pattern.compile("\\[(.*?)\\]");
				Matcher set_mat = set_pat.matcher(s_label);
				MathSet<MathList<AtomicEvent>> labelling = new MathSet<MathList<AtomicEvent>>();
				while (set_mat.find())
				{
					String[] ids = set_mat.group(1).split(",");
					MathList<AtomicEvent> list = new MathList<AtomicEvent>();
					for (String s_id : ids)
					{
						list.add(new AtomicEvent(s_id.trim()));
					}
					labelling.add(list);
				}
				graph.getLabelling().put(vertex_id, labelling);
			}
		}
		scanner.close();
		return graph;
	}


}
