package ca.uqac.lif.ecp;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class CayleyGraphFactory<T extends Event,U extends Object>
{
	/**
	 * The maximum number of iterations the search algorithm is allowed to
	 * do
	 */
	protected static final int MAX_ITERATIONS = 100000;
	
	public CayleyGraph<T,U> getGraph(TriagingFunction<T,U> f)
	{
		CayleyGraph<T,U> graph = new CayleyGraph<T,U>(); 
		Queue<VertexEventTracePair> to_explore = new LinkedList<VertexEventTracePair>();
		Queue<VertexEventTracePair> explored = new LinkedList<VertexEventTracePair>();
		Vertex<T> current_vertex = new Vertex<T>();
		Trace<T> current_trace = new Trace<T>();
		U category = f.getClass(current_trace);
		graph.add(current_vertex);
		graph.m_labelling.put(current_vertex.getId(), category);
		Set<T> nexts = getNextEvents();
		for (T e : nexts)
		{
			VertexEventTracePair vep = new VertexEventTracePair(current_vertex, current_trace, e);
			if (!explored.contains(vep))
			{
				to_explore.add(vep);
			}
		}
		for (int it_count = 0; !to_explore.isEmpty() && it_count < MAX_ITERATIONS; it_count++)
		{
			VertexEventTracePair vep = to_explore.remove();
			explored.add(vep);
			Vertex<T> source_vertex = vep.vertex;
			current_trace = new Trace<T>(vep.trace);
			current_trace.add(vep.event);
			category = f.getClass(current_trace);
			Vertex<T> target_vertex = graph.getFirstVertexWithLabelling(category);
			if (target_vertex == null)
			{
				target_vertex = new Vertex<T>();
				graph.add(target_vertex);
				graph.m_labelling.put(target_vertex.getId(), category);
			}
			Edge<T> edge = new Edge<T>(vep.event, target_vertex.getId());
			source_vertex.add(edge);
			nexts = getNextEvents();
			for (T e : nexts)
			{
				vep = new VertexEventTracePair(target_vertex, current_trace, e);
				if (!explored.contains(vep) && !to_explore.contains(vep))
				{
					to_explore.add(vep);
				}
			}
		}
		return graph;
	}
	
	protected class VertexEventTracePair
	{
		public Vertex<T> vertex;
		public Trace<T> trace;
		public T event;
		
		public VertexEventTracePair(Vertex<T> v, Trace<T> t, T e)
		{
			super();
			vertex = v;
			trace = t;
			event = e;
		}
		
		@Override
		public String toString()
		{
			StringBuilder out = new StringBuilder();
			out.append("<").append(vertex).append(",").append(trace).append(",").append(event).append(">");
			return out.toString();
		}
		
		@Override
		public int hashCode()
		{
			return 0;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof CayleyGraphFactory.VertexEventTracePair))
			{
				return false;
			}
			@SuppressWarnings("unchecked")
			VertexEventTracePair vetp = (VertexEventTracePair) o;
			// We do not perform equality on the trace; since we are in a Cayley
			// graph, source state + event is sufficient
			return vertex.equals(vetp.vertex) /*&& trace.equals(vetp.trace)*/ && event.equals(vetp.event);
		}
	}
	
	protected abstract Set<T> getNextEvents();
}
