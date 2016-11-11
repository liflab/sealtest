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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;

public class DijkstraShortestPaths<T extends Event> extends ShortestPaths<T>
{
	/**
	 * The maximum number of iterations allowed for the algorithm
	 */
	protected static final int s_maxIterations = 10000;
	
	public DijkstraShortestPaths(LabelledGraph<T> graph)
	{
		super(graph);
	}
	
	@Override
	public Map<Integer,Edge<T>> getShortestPaths()
	{
		Map<Integer,Integer> distance = new HashMap<Integer,Integer>();
		Map<Integer,Edge<T>> prev = new HashMap<Integer,Edge<T>>();
		Set<Integer> Q = new HashSet<Integer>();
		Vertex<T> initial_vertex = m_graph.getInitialVertex();
		for (Vertex<T> v : m_graph.getVertices())
		{
			Q.add(v.getId());
		}
		distance.put(initial_vertex.getId(), 0);
		for (int it_count = 0; it_count < s_maxIterations && !Q.isEmpty(); it_count++)
		{
			int u_id = getVertexWithMinimumDistance(Q, distance);
			Q.remove(u_id);
			Vertex<T> u = m_graph.getVertex(u_id);
			for (Edge<T> e : u.getEdges())
			{
				int v_id = e.getDestination();
				int alt = distance.get(u_id) + 1; // Could replace 1 by e.getWeight()
				if (!distance.containsKey(v_id) || alt < distance.get(v_id))
				{
					distance.put(v_id, alt);
					prev.put(v_id, e);
				}
			}
		}
		return prev;		
	}
	
	protected int getVertexWithMinimumDistance(Set<Integer> Q_id, Map<Integer,Integer> distance)
	{
		int min_dist = -1;
		int index = -1;
		for (Entry<Integer,Integer> e : distance.entrySet())
		{
			if (!Q_id.contains(e.getKey()))
			{
				continue;
			}
			int value = e.getValue();
			if (min_dist == -1 || value < min_dist)
			{
				min_dist = value;
				index = e.getKey();
			}
		}
		return index;
	}

}
