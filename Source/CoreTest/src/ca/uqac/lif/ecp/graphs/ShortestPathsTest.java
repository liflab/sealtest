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

import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.atomic.IntegerAtom;

/**
 * Unit tests for the shortest paths algorithm
 * 
 * @author Sylvain Hallé
 */
public class ShortestPathsTest 
{
	@Test
	public void test1()
	{
		Vertex<IntegerAtom> vertex;
		LabelledGraph<IntegerAtom> graph = new LabelledGraph<IntegerAtom>();
		vertex = new Vertex<IntegerAtom>(0);
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 1));
		vertex.add(new Edge<IntegerAtom>(0, new IntegerAtom(0), 2));
		graph.add(vertex);
		vertex = new Vertex<IntegerAtom>(1);
		vertex.add(new Edge<IntegerAtom>(1, new IntegerAtom(0), 2));
		graph.add(vertex);
		vertex = new Vertex<IntegerAtom>(2);
		vertex.add(new Edge<IntegerAtom>(2, new IntegerAtom(0), 0));
		graph.add(vertex);
		ShortestPaths<IntegerAtom> sp = new DijkstraShortestPaths<IntegerAtom>(graph);
		Map<Integer,Edge<IntegerAtom>> paths = sp.getShortestPaths();
		assertNotNull(paths);
		assertEquals(0, paths.get(1).getSource());
		assertEquals(0, paths.get(2).getSource());
		assertFalse(paths.containsKey(0));
	}
}
