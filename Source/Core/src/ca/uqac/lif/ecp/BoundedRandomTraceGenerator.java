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

import java.util.Random;

/**
 * Trace generator that picks events randomly. The trace generator is
 * instructed to generate <i>n</i> traces, each of length <i>m</i>, by
 * picking at random each event in the set of possible events.
 * 
 * @param <T> The type of the events in the traces that are generated
 */
public abstract class BoundedRandomTraceGenerator<T extends Event> extends RandomTraceGenerator<T> 
{	
	/**
	 * The number of traces to generate
	 */
	protected int m_numTraces;
	
	/**
	 * The length of the traces to generate
	 */
	protected int m_traceLength;
	
	public BoundedRandomTraceGenerator(Random random, int num_traces, int trace_length)
	{
		super(random);
		m_numTraces = num_traces;
		m_traceLength = trace_length;
	}
	
	@Override
	public TestSuite<T> generateTraces()
	{
		TestSuite<T> trace_set = new TestSuite<T>();
		for (int trace_count = 0; trace_count < m_numTraces; trace_count++)
		{
			Trace<T> trace = new Trace<T>();
			for (int len_count = 0; len_count < m_traceLength; len_count++)
			{
				T event = nextEvent(m_random);
				trace.add(event);
			}
			trace_set.add(trace);
		}
		return trace_set;
	}

}
