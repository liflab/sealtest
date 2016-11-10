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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathSet;

/**
 * Solves the Steiner tree problem by performing a breadth-first traversal of
 * the graph from its root. The resulting tree will be the union of the shortest
 * path from the root to each of the important vertices.
 * <p>
 * Note that this method of solving the problem is simple, but probably not
 * very optimal.
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events that are the labels of the edges
 * @param <U> The type of categories of the underlying triaging function
 */
public class BreadthFirstSteinerTree<T extends Event, U> extends SteinerTree<T, U>
{
	/**
	 * The max depth for the breadth first traversal
	 */
	protected int m_maxDepth = 1000;
	
	public BreadthFirstSteinerTree(CayleyGraph<T, U> graph, MathSet<Integer> vertices)
	{
		super(graph, vertices);
	}
	
	/**
	 * Sets the maximum depth for the breadth first traversal
	 * @param max_depth The depth
	 */
	public void setDepth(int max_depth)
	{
		m_maxDepth = max_depth;
	}

	@Override
	public CayleyGraph<T, U> getTree() 
	{
		SteinerVisitor visitor = new SteinerVisitor();
		visitor.start();
		Set<List<Edge<T>>> paths = visitor.getPaths();
		CayleyGraph<T,U> out = getSubgraph(paths);
		return out;
	}
	
	/**
	 * Creates a subgraph from graph, made by including only vertices and
	 * edges contained in a given set of paths
	 * @param paths The set of paths
	 * @return The subgraph
	 */
	public CayleyGraph<T,U> getSubgraph(Set<List<Edge<T>>> paths)
	{
		CayleyGraph<T,U> out = new CayleyGraph<T,U>();
		boolean first = true;
		for (List<Edge<T>> path : paths)
		{
			if (first)
			{
				first = false;
				Edge<T> first_edge = path.get(0);
				int source_id = first_edge.getSource();
				Vertex<T> v_first = new Vertex<T>(source_id);
				out.add(v_first);
				out.setInitialVertexId(source_id);
			}
			for (Edge<T> e : path)
			{
				int source_id = e.getSource();
				Vertex<T> v_source = out.getVertex(source_id);
				if (v_source == null)
				{
					// Not supposed to happen, but still
					v_source = new Vertex<T>(source_id);
					out.add(v_source);
				}
				v_source.add(e);
				int dest_id = e.getDestination();
				Vertex<T> v_dest = out.getVertex(dest_id);
				if (v_dest == null)
				{
					v_dest = new Vertex<T>(dest_id);
					out.add(v_dest);
				}
			}
		}
		return out;
	}
	
	protected class SteinerVisitor extends BreadthFirstVisitor<T>
	{
		protected Set<List<Edge<T>>> m_selectedPaths;
		
		SteinerVisitor()
		{
			// Visit each node only once
			super(true);
			m_selectedPaths = new HashSet<List<Edge<T>>>();
		}
		
		public void start()
		{
			super.start(m_graph, m_graph.getInitialVertex().getId(), m_maxDepth);
		}
		
		@Override
		public void visit(ArrayList<Edge<T>> path)
		{
			if (path.isEmpty())
			{
				return;
			}
			Edge<T> last_edge = path.get(path.size() - 1);
			int id_vertex = last_edge.getDestination();
			// Is this vertex part of the important vertices?
			if (m_importantVertices.contains(id_vertex))
			{
				m_selectedPaths.add(path);
			}
		}
		
		public Set<List<Edge<T>>> getPaths()
		{
			return m_selectedPaths;
		}
	}

}
