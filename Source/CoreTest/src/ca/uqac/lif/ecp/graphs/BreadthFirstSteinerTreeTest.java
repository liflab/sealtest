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
public class BreadthFirstSteinerTreeTest 
{
	@Test
	public void test1()
	{
		Automaton g = loadAutomaton("test1.dot");
		assertEquals(4, g.getVertexCount());
		assertEquals(4, g.getEdgeCount());
		MathSet<Integer> important_vertices = new MathSet<Integer>(0, 1);
		BreadthFirstSteinerTree<AtomicEvent,String> solver = new BreadthFirstSteinerTree<AtomicEvent,String>(g, important_vertices);
		CayleyGraph<AtomicEvent,String> tree = solver.getTree();
		assertNotNull(tree);
		assertEquals(2, tree.getVertexCount());
		assertEquals(1, tree.getEdgeCount());
	}
	
	@Test
	public void test2()
	{
		Automaton g = loadAutomaton("test1.dot");
		assertEquals(4, g.getVertexCount());
		assertEquals(4, g.getEdgeCount());
		MathSet<Integer> important_vertices = new MathSet<Integer>(1, 2);
		BreadthFirstSteinerTree<AtomicEvent,String> solver = new BreadthFirstSteinerTree<AtomicEvent,String>(g, important_vertices);
		CayleyGraph<AtomicEvent,String> tree = solver.getTree();
		assertNotNull(tree);
		assertEquals(3, tree.getVertexCount());
		assertEquals(2, tree.getEdgeCount());
	}
	
	@Test
	public void test3()
	{
		Automaton g = loadAutomaton("test1.dot");
		assertEquals(4, g.getVertexCount());
		assertEquals(4, g.getEdgeCount());
		MathSet<Integer> important_vertices = new MathSet<Integer>(3);
		BreadthFirstSteinerTree<AtomicEvent,String> solver = new BreadthFirstSteinerTree<AtomicEvent,String>(g, important_vertices);
		CayleyGraph<AtomicEvent,String> tree = solver.getTree();
		assertNotNull(tree);
		assertEquals(3, tree.getVertexCount());
		assertEquals(2, tree.getEdgeCount());
	}
	
	@Test
	public void test4()
	{
		Automaton g = loadAutomaton("test3.dot");
		assertEquals(3, g.getVertexCount());
		assertEquals(3, g.getEdgeCount());
		MathSet<Integer> important_vertices = new MathSet<Integer>(0, 1, 2);
		BreadthFirstSteinerTree<AtomicEvent,String> solver = new BreadthFirstSteinerTree<AtomicEvent,String>(g, important_vertices);
		CayleyGraph<AtomicEvent,String> tree = solver.getTree();
		assertNotNull(tree);
		assertEquals(3, tree.getVertexCount());
		assertEquals(2, tree.getEdgeCount());
	}
	
	public Automaton loadAutomaton(String filename)
	{
		 InputStream is = this.getClass().getResourceAsStream(TestSettings.s_dataFolder + filename);
		 Scanner scanner = new Scanner(is);
		 return Automaton.parseDot(scanner);
	}
}
