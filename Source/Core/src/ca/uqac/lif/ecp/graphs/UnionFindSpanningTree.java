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

import java.util.Iterator;
import java.util.List;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;

/**
 * Solves the spanning tree problem using the union-find technique 
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events that are the labels of the edges
 * @param <U> The type of categories of the underlying triaging function
 */
public class UnionFindSpanningTree<T extends Event,U> extends SpanningTree<T,U> 
{
	public UnionFindSpanningTree(CayleyGraph<T, U> graph) 
	{
		super(graph);
	}

	public CayleyGraph<T,U> getSpanningTree()
	{
		ForestNode<T> forest = new ForestNode<T>(null);
		List<Edge<T>> edges = forest.getOrderedSpanningTree(m_graph);
		CayleyGraph<T,U> tree = new CayleyGraph<T,U>(m_graph);
		for (Vertex<T> v : tree.getVertices())
		{
			Iterator<Edge<T>> e_it = v.getEdges().iterator();
			while (e_it.hasNext())
			{
				Edge<T> e = e_it.next();
				if (!edges.contains(e))
				{
					// This edge is not in the list; take it off
					e_it.remove();
				}
			}
		}
		return tree;
	}

}
