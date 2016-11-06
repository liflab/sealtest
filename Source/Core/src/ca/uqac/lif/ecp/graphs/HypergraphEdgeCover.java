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
 * Solver for the hypergraph edge covering problem. Different descendents
 * of this abstract class will solve the problem using different algorithms.
 * @author Sylvain Hallé
 *
 */
public abstract class HypergraphEdgeCover 
{
	/**
	 * The hypergraph this solver will work on
	 */
	protected Hypergraph m_graph;
	
	public HypergraphEdgeCover(Hypergraph g)
	{
		super();
		m_graph = g;
	}
	
	/**
	 * Gets a covering of the hyperedges
	 * @return The covering
	 */
	public abstract MathSet<Hyperedge> getCover();
	
	/**
	 * Gets a new, empty instance of this solver
	 * @param g The hypergraph to solve
	 * @return The solver
	 */
	public abstract HypergraphEdgeCover newSolver(Hypergraph g);
}
