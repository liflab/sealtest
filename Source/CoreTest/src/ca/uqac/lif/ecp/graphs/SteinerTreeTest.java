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
package ca.uqac.lif.ecp.graphs;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.structures.MathSet;

/**
 * Unit tests for the breadth-first Steiner tree solver
 * 
 * @author Sylvain Hallé
 */
public class SteinerTreeTest 
{
	@Test
	public void test1B()
	{
		testGraph("test1.dot", new MathSet<Integer>(0, 1), new BreadthFirstSteinerTree<AtomicEvent,String>(null, null), 2, 1);
	}
	
	@Test
	public void test2B()
	{
		testGraph("test1.dot", new MathSet<Integer>(1, 2), new BreadthFirstSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}
	
	@Test
	public void test3B()
	{
		testGraph("test1.dot", new MathSet<Integer>(3), new BreadthFirstSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}
	
	@Test
	public void test4B()
	{
		testGraph("test3.dot", new MathSet<Integer>(0, 1, 2), new BreadthFirstSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}
	
	@Test
	public void test1D()
	{
		testGraph("test1.dot", new MathSet<Integer>(0, 1), new DijkstraSteinerTree<AtomicEvent,String>(null, null), 2, 1);
	}
	
	@Test
	public void test2D()
	{
		testGraph("test1.dot", new MathSet<Integer>(1, 2), new DijkstraSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}
	
	@Test
	public void test3D()
	{
		testGraph("test1.dot", new MathSet<Integer>(3), new DijkstraSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}
	
	@Test
	public void test4D()
	{
		testGraph("test3.dot", new MathSet<Integer>(0, 1, 2), new DijkstraSteinerTree<AtomicEvent,String>(null, null), 3, 2);
	}

	
	public void testGraph(String filename, MathSet<Integer> important_vertices, SteinerTree<AtomicEvent,String> solver, int vertex_count, int edge_count)
	{
		Automaton g = loadAutomaton(filename);
		SteinerTree<AtomicEvent,String> solver_instance = solver.newSolver(g, important_vertices);
		CayleyGraph<AtomicEvent,String> tree = solver_instance.getTree();
		assertNotNull(tree);
		assertEquals(vertex_count, tree.getVertexCount());
		assertEquals(edge_count, tree.getEdgeCount());		
	}
	
	public Automaton loadAutomaton(String filename)
	{
		 InputStream is = this.getClass().getResourceAsStream(TestSettings.s_dataFolder + filename);
		 Scanner scanner = new Scanner(is);
		 return Automaton.parseDot(scanner);
	}
}
