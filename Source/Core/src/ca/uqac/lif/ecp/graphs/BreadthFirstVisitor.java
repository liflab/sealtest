package ca.uqac.lif.ecp.graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;

public abstract class BreadthFirstVisitor<T extends Event>
{
	public void start(LabelledGraph<T> g, int start_id, int max_depth)
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
				Vertex<T> v = g.getVertex(last_edge.getDestination());
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
