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

public class HypergraphTraceGeneratorTest
{
	@Test
	public void test1()
	{
		// Begin graph
		Vertex<IntegerAtom> vertex;
		CayleyGraph<IntegerAtom,AtomicEvent> graph = new CayleyGraph<IntegerAtom,AtomicEvent>();
		vertex = new Vertex<IntegerAtom>(0);
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(1), 1));
		graph.add(vertex);
		vertex = new Vertex<IntegerAtom>(1);
		graph.add(vertex);
		CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
		labelling.put(0, new MathSet<AtomicEvent>(new AtomicEvent("a")));
		labelling.put(1, new MathSet<AtomicEvent>(new AtomicEvent("b")));
		graph.setLabelling(labelling);
		// End graph
		HypergraphTraceGenerator<IntegerAtom,AtomicEvent> generator = new HypergraphTraceGenerator<IntegerAtom,AtomicEvent>(graph);
		TestSuite<IntegerAtom> suite = generator.generateTraces();
		assertNotNull(suite);
		assertEquals(1, suite.size());
	}
	
	@Test
	public void test2()
	{
		// Begin graph
		Vertex<IntegerAtom> vertex;
		CayleyGraph<IntegerAtom,AtomicEvent> graph = new CayleyGraph<IntegerAtom,AtomicEvent>();
		vertex = new Vertex<IntegerAtom>(0);
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 0));
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(1), 1));
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(2), 2));
		graph.add(vertex);
		graph.add(new Vertex<IntegerAtom>(1));
		graph.add(new Vertex<IntegerAtom>(2));
		CayleyVertexLabelling<AtomicEvent> labelling = new CayleyVertexLabelling<AtomicEvent>();
		labelling.put(0, new MathSet<AtomicEvent>(new AtomicEvent("a")));
		labelling.put(1, new MathSet<AtomicEvent>(new AtomicEvent("b")));
		labelling.put(2, new MathSet<AtomicEvent>(new AtomicEvent("c")));
		graph.setLabelling(labelling);
		// End graph
		HypergraphTraceGenerator<IntegerAtom,AtomicEvent> generator = new HypergraphTraceGenerator<IntegerAtom,AtomicEvent>(graph);
		TestSuite<IntegerAtom> suite = generator.generateTraces();
		assertNotNull(suite);
		assertEquals(2, suite.size());
	}
}
