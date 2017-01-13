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
package ca.uqac.lif.ecp.ltl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.CoverageMetric;
import ca.uqac.lif.ecp.GreedyTraceGenerator;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

/**
 * Greedy generator when the input specification is a finite-state automaton.
 * @author Sylvain Hallé
 */
public class GreedyLtlGenerator extends GreedyTraceGenerator<AtomicEvent>
{
	/**
	 * The LTL formula to generate against
	 */
	protected final Operator<AtomicEvent> m_formula;
	
	/**
	 * The set of possible events. We store them as a list to make it
	 * easier to randomly pick one of them.
	 */
	protected final List<AtomicEvent> m_alphabet;

	public GreedyLtlGenerator(Operator<AtomicEvent> op, Alphabet<AtomicEvent> alph, Random random, CoverageMetric<AtomicEvent, Float> metric) 
	{
		super(random, metric);
		m_formula = op;
		m_alphabet = new ArrayList<AtomicEvent>(alph.size());
		m_alphabet.addAll(alph);
	}

	@Override
	public Trace<AtomicEvent> generateTrace(int length)
	{
		Trace<AtomicEvent> trace = new Trace<AtomicEvent>();
		int pos = 0;
		for (int l = 0; l < length; l++)
		{
			pos = m_random.nextInt(m_alphabet.size());
			trace.add(m_alphabet.get(pos));
		}
		return trace;
	}
}
