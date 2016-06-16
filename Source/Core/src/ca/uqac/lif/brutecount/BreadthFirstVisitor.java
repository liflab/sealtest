package ca.uqac.lif.brutecount;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public abstract class BreadthFirstVisitor
{
	public void start(Graph g, String start_label, int max_depth)
	{
		Vertex start = g.getVertex(start_label);
		Queue<ArrayList<Edge>> paths = new ArrayDeque<ArrayList<Edge>>();
		for (Edge e : start.m_outEdges)
		{
			ArrayList<Edge> l = new ArrayList<Edge>();
			l.add(e);
			paths.add(l);
		}
		for (int depth = 0; depth < max_depth; depth++)
		{
			Queue<ArrayList<Edge>> new_paths = new ArrayDeque<ArrayList<Edge>>();
			while (!paths.isEmpty())
			{
				ArrayList<Edge> path = paths.remove();
				visit(path);
				Edge last_edge = path.get(path.size() - 1);
				Vertex v = g.getVertex(last_edge.m_destination);
				for (Edge e : v.m_outEdges)
				{
					ArrayList<Edge> new_path = new ArrayList<Edge>();
					new_path.addAll(path);
					new_path.add(e);
					new_paths.add(new_path);
				}
			}
			paths = new_paths;
		}
	}
	
	public abstract void visit(ArrayList<Edge> path);
}
