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
package ca.uqac.lif.ecp.atomic;

import java.util.Random;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.BoundedRandomTraceGenerator;

public class AtomicBoundedRandomGenerator extends BoundedRandomTraceGenerator<AtomicEvent> 
{
	/**
	 * The alphabet to pick events from
	 */
	protected AtomicEvent[] m_alphabet;
	
	public AtomicBoundedRandomGenerator(Random random, int num_traces, int trace_length, Alphabet<AtomicEvent> alphabet) 
	{
		super(random, num_traces, trace_length);
		m_alphabet = alphabet.toArray(new AtomicEvent[alphabet.size()]);
	}

	@Override
	protected AtomicEvent nextEvent(Random random)
	{
		return m_alphabet[random.nextInt(m_alphabet.length)];
	}

}
