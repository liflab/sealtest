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
package ca.uqac.lif.ecp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class BreadthFirstVisitor<T extends Event,U extends Object>
{
	public void start(CayleyGraph<T,U> g, int start_id, int max_depth)
	{
		Vertex<T> start = g.getVertex(start_id);
		Queue<ArrayList<Edge<T>>> paths = new ArrayDeque<ArrayList<Edge<T>>>();
		for (Edge<T> e : start.m_outEdges)
		{
			ArrayList<Edge<T>> l = new ArrayList<Edge<T>>();
			l.add(e);
			paths.add(l);
		}
		for (int depth = 0; depth < max_depth; depth++)
		{
			Queue<ArrayList<Edge<T>>> new_paths = new ArrayDeque<ArrayList<Edge<T>>>();
			while (!paths.isEmpty())
			{
				ArrayList<Edge<T>> path = paths.remove();
				visit(path);
				Edge<T> last_edge = path.get(path.size() - 1);
				Vertex<T> v = g.getVertex(last_edge.m_destination);
				for (Edge<T> e : v.m_outEdges)
				{
					ArrayList<Edge<T>> new_path = new ArrayList<Edge<T>>();
					new_path.addAll(path);
					new_path.add(e);
					new_paths.add(new_path);
				}
			}
			paths = new_paths;
			depthStep(depth + 1);
		}
	}
	
	public void depthStep(int depth)
	{
		// Do nothing
	}
	
	public abstract void visit(ArrayList<Edge<T>> path);
}
