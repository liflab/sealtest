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

import ca.uqac.lif.ecp.graphs.Hypergraph.Hyperedge;
import ca.uqac.lif.structures.MathSet;

/**
 * Unit tests for the greedy hypergraph edge cover solver
 * 
 * @author Sylvain Hallé
 */
public class GreedyHypergraphEdgeCoverTest 
{
	@Test
	public void test1()
	{
		Hypergraph g = loadGraph("test1.hyg");
		assertEquals(3, g.getVertexCount());
		assertEquals(3, g.getEdgeCount());
		GreedyHypergraphEdgeCover solver = new GreedyHypergraphEdgeCover(g);
		MathSet<Hyperedge> cover = solver.getCover();
		assertNotNull(cover);
		assertEquals(2, cover.size());
	}
	
	@Test
	public void test2()
	{
		Hypergraph g = loadGraph("test2.hyg");
		assertEquals(6, g.getVertexCount());
		assertEquals(4, g.getEdgeCount());
		GreedyHypergraphEdgeCover solver = new GreedyHypergraphEdgeCover(g);
		MathSet<Hyperedge> cover = solver.getCover();
		assertNotNull(cover);
		assertEquals(2, cover.size());
	}
	
	public Hypergraph loadGraph(String filename)
	{
		 InputStream is = this.getClass().getResourceAsStream(TestSettings.s_dataFolder + filename);
		 Scanner scanner = new Scanner(is);
		 return Hypergraph.parse(scanner);
	}
}
