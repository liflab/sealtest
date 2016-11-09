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

import org.junit.Test;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.IntegerAtom;

public class LabelledGraphTest 
{
	@Test
	public void testDepth1()
	{
		LabelledGraph<IntegerAtom> graph = getGraph1();
		assertEquals(2, graph.getDepth());
	}
	
	@Test
	public void testDepth2()
	{
		LabelledGraph<IntegerAtom> graph = getGraph2();
		assertEquals(2, graph.getDepth());
	}
	
	public LabelledGraph<IntegerAtom> getGraph1()
	{
		LabelledGraph<IntegerAtom> graph = new LabelledGraph<IntegerAtom>();
		graph.add(newVertex(0, newEdge(0, 0, 1)));
		graph.add(newVertex(1, newEdge(1, 0, 2)));
		graph.add(newVertex(2));
		return graph;
	}
	
	public LabelledGraph<IntegerAtom> getGraph2()
	{
		LabelledGraph<IntegerAtom> graph = new LabelledGraph<IntegerAtom>();
		graph.add(newVertex(0, newEdge(0, 0, 1)));
		graph.add(newVertex(1, newEdge(1, 0, 2), newEdge(1, 0, 0)));
		graph.add(newVertex(2, newEdge(2, 0, 0)));
		return graph;
	}
	
	@SuppressWarnings("unchecked")
	public Vertex<IntegerAtom> newVertex(int id, Edge<?> ... edges)
	{
		Vertex<IntegerAtom> vertex = new Vertex<IntegerAtom>(id);
		for (Edge<?> edge : edges)
		{
			vertex.add((Edge<IntegerAtom>) edge);
		}
		return vertex;
	}
	
	public Edge<IntegerAtom> newEdge(int source, int label, int destination)
	{
		return new Edge<IntegerAtom>(source, new IntegerAtom(label), destination);
	}
}
