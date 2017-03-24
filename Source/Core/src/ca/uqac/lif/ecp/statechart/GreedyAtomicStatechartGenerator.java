/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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
package ca.uqac.lif.ecp.statechart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.uqac.lif.ecp.CoverageMetric;
import ca.uqac.lif.ecp.GreedyTraceGenerator;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

/**
 * Greedy generator when the input specification is an atomic UML statechart.
 * In such a case, candidate traces are generated by performing a random walk
 * in the given statechart.
 * @author Sylvain Hallé
 */
public class GreedyAtomicStatechartGenerator extends GreedyTraceGenerator<AtomicEvent>
{
	/**
	 * The statechart used
	 */
	protected final Statechart<AtomicEvent> m_statechart;
	
	/**
	 * The alphabet of this statechart
	 */
	protected List<AtomicEvent> m_alphabet;
	
	/**
	 * Creates a greedy generator.
	 * @param aut The automaton specification
	 * @param random A random number generator used to pick events and values
	 * @param metric The coverage metric against which to compare candidate
	 * solutions
	 */
	public GreedyAtomicStatechartGenerator(Statechart<AtomicEvent> aut, Random random, CoverageMetric<AtomicEvent,Float> metric) 
	{
		super(random, metric);
		m_statechart = aut;
		m_alphabet = new ArrayList<AtomicEvent>();
		if (aut instanceof AtomicStatechart)
		{
			m_alphabet.addAll(((AtomicStatechart) aut).getAlphabet());
		}
	}
	
	@Override
	public int pickLength()
	{
		return m_random.nextInt(m_statechart.getEdgeCount());
	}

	@Override
	public Trace<AtomicEvent> generateTrace(int length) 
	{
		Trace<AtomicEvent> trace = new Trace<AtomicEvent>();
		m_statechart.reset();
		for (int steps = 0; steps < length; steps++)
		{
			int chosen_edge_index = m_random.nextInt(m_alphabet.size());
			AtomicEvent chosen_edge = m_alphabet.get(chosen_edge_index);
			trace.add(chosen_edge);
			m_statechart.takeTransition(chosen_edge);
		}
		return trace;
	}
}