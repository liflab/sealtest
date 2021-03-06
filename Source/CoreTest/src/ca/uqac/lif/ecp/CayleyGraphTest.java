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
package ca.uqac.lif.ecp;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.IntegerAtom;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * Unit tests for the Cayley graph class
 * 
 * @author Sylvain Hallé
 */
public class CayleyGraphTest 
{
	/*
	 * Twice the same graph
	 */
	@Test
	public void testIsomorphism1()
	{
		Vertex<IntegerAtom> vertex;
		// First graph
		CayleyGraph<IntegerAtom,AtomicEvent> graph1 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(0);
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 0));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(1), 1));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(2), 2));
			graph1.add(vertex);
			graph1.add(new Vertex<IntegerAtom>(1));
			graph1.add(new Vertex<IntegerAtom>(2));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(0, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(1, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			labelling.put(2, new MathSet<AtomicEvent>(new AtomicEvent("c")));
			graph1.setLabelling(labelling);
		}
		// Second graph; recall that vertex IDs continue to be incremented,
		// so that graph2 does not have the same IDs as graph 1
		CayleyGraph<IntegerAtom,AtomicEvent> graph2 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(3);
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(0), 3));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(1), 4));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(2), 5));
			graph2.add(vertex);
			graph2.add(new Vertex<IntegerAtom>(4));
			graph2.add(new Vertex<IntegerAtom>(5));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(3, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(4, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			labelling.put(5, new MathSet<AtomicEvent>(new AtomicEvent("c")));
			graph2.setLabelling(labelling);
		}
		assertTrue(graph1.isIsomorphicTo(graph1));
		assertTrue(graph1.isIsomorphicTo(graph2));
		assertTrue(graph2.isIsomorphicTo(graph1));
	}
	
	/*
	 * One graph is missing an edge
	 */
	@Test
	public void testIsomorphism2()
	{
		Vertex<IntegerAtom> vertex;
		// First graph
		CayleyGraph<IntegerAtom,AtomicEvent> graph1 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(0);
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 0));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(1), 1));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(2), 2));
			graph1.add(vertex);
			graph1.add(new Vertex<IntegerAtom>(1));
			graph1.add(new Vertex<IntegerAtom>(2));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(0, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(1, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			graph1.setLabelling(labelling);
		}
		// Second graph; recall that vertex IDs continue to be incremented,
		// so that graph2 does not have the same IDs as graph 1
		CayleyGraph<IntegerAtom,AtomicEvent> graph2 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(3);
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(0), 3));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(1), 4));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(2), 5));
			graph2.add(vertex);
			graph2.add(new Vertex<IntegerAtom>(4));
			graph2.add(new Vertex<IntegerAtom>(5));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(3, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(4, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			labelling.put(5, new MathSet<AtomicEvent>(new AtomicEvent("c")));
			graph2.setLabelling(labelling);
		}
		assertFalse(graph1.isIsomorphicTo(graph2));
		assertFalse(graph2.isIsomorphicTo(graph1));
	}
	
	/*
	 * Twice the same graph with two edges swapped
	 */
	@Test
	public void testIsomorphism3()
	{
		Vertex<IntegerAtom> vertex;
		// First graph
		CayleyGraph<IntegerAtom,AtomicEvent> graph1 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(0);
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 0));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(1), 1));
			vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(2), 2));
			graph1.add(vertex);
			graph1.add(new Vertex<IntegerAtom>(1));
			graph1.add(new Vertex<IntegerAtom>(2));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(0, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(1, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			labelling.put(2, new MathSet<AtomicEvent>(new AtomicEvent("c")));
			graph1.setLabelling(labelling);
		}
		// Second graph; recall that vertex IDs continue to be incremented,
		// so that graph2 does not have the same IDs as graph 1
		CayleyGraph<IntegerAtom,AtomicEvent> graph2 = new CayleyGraph<IntegerAtom,AtomicEvent>();
		{
			vertex = new Vertex<IntegerAtom>(3);
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(0), 3));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(2), 4));
			vertex.add(new Edge<IntegerAtom>(3, new IntegerAtom(1), 5));
			graph2.add(vertex);
			graph2.add(new Vertex<IntegerAtom>(4));
			graph2.add(new Vertex<IntegerAtom>(5));
			CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
			labelling.put(3, new MathSet<AtomicEvent>(new AtomicEvent("a")));
			labelling.put(4, new MathSet<AtomicEvent>(new AtomicEvent("c")));
			labelling.put(5, new MathSet<AtomicEvent>(new AtomicEvent("b")));
			graph2.setLabelling(labelling);
		}
		assertTrue(graph1.isIsomorphicTo(graph2));
		assertTrue(graph2.isIsomorphicTo(graph1));
	}
}
