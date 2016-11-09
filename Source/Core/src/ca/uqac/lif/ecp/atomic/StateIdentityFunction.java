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

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.structures.MathSet;

/**
 * Triaging function where the class of a trace is the state of
 * the automaton you end on while reading it
 */
public class StateIdentityFunction extends AutomatonFunction<Integer> 
{
	public StateIdentityFunction(Automaton a)
	{
		super(a);
	}

	@Override
	public MathSet<Integer> getStartClass()
	{
		MathSet<Integer> out = new MathSet<Integer>();
		out.add(0);
		return out;
	}
	
	@Override
	public MathSet<Integer> processTransition(Edge<AtomicEvent> edge)
	{
		MathSet<Integer> out = new MathSet<Integer>();
		out.add(edge.getDestination());
		return out;
	}

}
