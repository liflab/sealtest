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

import ca.uqac.lif.ecp.graphs.Hypergraph.Hyperedge;
import ca.uqac.lif.structures.MathSet;

/**
 * Solves the hypergraph edge cover by a greedy approach. The algorithm simply
 * picks the hyperedge that covers the most remaining vertices.
 * 
 * @author Sylvain Hallé
 */
public class GreedyHypergraphEdgeCover extends HypergraphEdgeCover
{
	public GreedyHypergraphEdgeCover(Hypergraph g)
	{
		super(g);
	}

	@Override
	public MathSet<Hyperedge> getCover() 
	{
		MathSet<Hyperedge> solution = new MathSet<Hyperedge>();
		MathSet<Hyperedge> available_edges = new MathSet<Hyperedge>(m_graph.m_edges);
		MathSet<Integer> uncovered_vertices = new MathSet<Integer>(m_graph.m_vertices);
		int max_loops = m_graph.m_vertices.size();
		for (int loop_count = 0; loop_count < max_loops; loop_count++)
		{
			Hyperedge candidate_h = null;
			int candidate_c = 0;
			for (Hyperedge e : available_edges)
			{
				int num_covered = intersect(e, uncovered_vertices);
				if (num_covered > candidate_c)
				{
					// Found a better candidate
					candidate_c = num_covered;
					candidate_h = e;
				}
			}
			if (candidate_h == null)
			{
				// We found no candidate: useless to continue
				break;
			}
			uncovered_vertices.removeAll(candidate_h);
			solution.add(candidate_h);
		}
		return solution;
	}
	
	/**
	 * Counts of many elements of x are present in y
	 * @param x A first set
	 * @param y A second set
	 * @return The number
	 */
	public static int intersect(MathSet<Integer> x, MathSet<Integer> y)
	{
		int n = 0;
		for (int e : x)
		{
			if (y.contains(e))
			{
				n++;
			}
		}
		return n;
	}

	@Override
	public HypergraphEdgeCover newSolver(Hypergraph g) 
	{
		return new GreedyHypergraphEdgeCover(g);
	}

}
