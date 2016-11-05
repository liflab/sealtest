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

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;

/**
 * Solves the spanning tree problem. Different descendents of this abstract
 * class will solve the problem using different algorithms. 
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events that are the labels of the edges
 * @param <U> The type of categories of the underlying triaging function
 */
public abstract class SpanningTree<T extends Event,U> extends CayleyGraphSolver<T,U>
{
	/**
	 * Instantiates a new solver
	 * @param graph The graph this solver will work on
	 */
	public SpanningTree(CayleyGraph<T,U> graph)
	{
		super(graph);
	}
		
	/**
	 * Gets the spanning tree from the solver's graph
	 * @return The tree. It is assumed that the graph from which the tree
	 *   is constructed has a single initial node; therefore this node is
	 *   also the root of the returned tree.
	 */
	public abstract CayleyGraph<T,U> getSpanningTree();
	
}
