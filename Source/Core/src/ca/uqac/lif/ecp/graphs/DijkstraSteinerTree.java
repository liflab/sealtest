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

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathSet;

/**
 * Solves the directed Steiner tree problem by using a small variant
 * of Dijkstra's algorithm
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class DijkstraSteinerTree<T extends Event,U> extends SteinerTree<T,U> 
{
	/**
	 * Sufficiently large value to stand for "infinity" in Dijkstra's
	 * algorithm
	 */
	protected static final int INFINITY = 100000;
	
	/**
	 * The maximum number of iterations allowed for the algorithm
	 */
	protected static final int s_maxIterations = 10000;
	
	public DijkstraSteinerTree(CayleyGraph<T, U> graph, MathSet<Integer> vertices) 
	{
		super(graph, vertices);
	}
	
	@Override
	public CayleyGraph<T,U> getTree() 
	{
		CayleyGraph<T,U> out_graph = new CayleyGraph<T,U>();
		ShortestPaths<T> dsp = new DijkstraShortestPaths<T>(m_graph);
		Map<Integer,Edge<T>> paths = dsp.getShortestPaths();
		Map<Integer,Vertex<T>> vertices = new HashMap<Integer,Vertex<T>>();
		for (Edge<T> edge : paths.values())
		{
			int dest_id = edge.getDestination();
			if (m_importantVertices.contains(dest_id))
			{
				moveUp(edge, paths, vertices);
			}
		}
		out_graph.addAll(vertices.values());
		out_graph.setInitialVertexId(m_graph.getInitialVertex().getId());
		return out_graph;
	}
	
	public void moveUp(Edge<T> start_edge, Map<Integer,Edge<T>> paths, Map<Integer,Vertex<T>> vertices)
	{
		Edge<T> edge = start_edge;
		getOrCreate(start_edge.getDestination(), vertices);
		for (int it_count = 0; it_count < paths.size(); it_count++)
		{
			int source_id = edge.getSource();
			if (vertices.containsKey(source_id))
			{
				// We are back to a node we already added
				Vertex<T> source_vertex = vertices.get(source_id);
				source_vertex.add(edge);
				break;
			}
			Vertex<T> source_vertex = new Vertex<T>(source_id);
			source_vertex.add(edge);
			vertices.put(source_id, source_vertex);
			int dest_id = edge.getDestination();
			getOrCreate(dest_id, vertices);
			if (!paths.containsKey(source_id))
			{
				break;
			}
			edge = paths.get(source_id);
		}
	}
	
	protected Vertex<T> getOrCreate(int id, Map<Integer,Vertex<T>> vertices)
	{
		Vertex<T> source_vertex = null;
		if (vertices.containsKey(id))
		{
			source_vertex = vertices.get(id);
		}
		else
		{
			source_vertex = new Vertex<T>(id);
			vertices.put(id, source_vertex);
		}
		return source_vertex;
	}

	@Override
	public DijkstraSteinerTree<T,U> newSolver(CayleyGraph<T,U> graph, MathSet<Integer> vertices) 
	{
		return new DijkstraSteinerTree<T,U>(graph, vertices);
	}
}
