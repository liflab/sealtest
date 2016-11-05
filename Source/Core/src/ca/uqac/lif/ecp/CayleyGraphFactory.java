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

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.structures.MathSet;

/**
 * Generates a Cayley graph using a generic algorithm. This procedure works
 * only if the undelrying triaging function respects the "monoid condition"
 * described in the paper. Otherwise, it may not terminate, or may return an
 * incorrect graph. 
 * @author Sylvain Hallé
 *
 * @param <T> The type of the events; events are used as edge labels
 * @param <U> The type of the categories of the triaging function. The 
 *   labelling of the states in the resulting graph will be made of
 *   <em>sets</em> of elements of type <code>U</code>
 * 
 */
public abstract class CayleyGraphFactory<T extends Event,U extends Object>
{
	/**
	 * The maximum number of iterations the search algorithm is allowed to
	 * do
	 */
	protected static final int MAX_ITERATIONS = 1000;
	
	public CayleyGraph<T,U> getGraph(TriagingFunction<T,U> f)
	{
		CayleyGraph<T,U> graph = new CayleyGraph<T,U>(); 
		Queue<VertexEventTracePair> to_explore = new LinkedList<VertexEventTracePair>();
		Queue<VertexEventTracePair> explored = new LinkedList<VertexEventTracePair>();
		Vertex<T> current_vertex = new Vertex<T>();
		Trace<T> current_trace = new Trace<T>();
		MathSet<U> category = f.getClass(current_trace);
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
			Edge<T> edge = new Edge<T>(source_vertex.getId(), vep.event, target_vertex.getId());
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
