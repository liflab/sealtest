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
package ca.uqac.lif.ecp.lab.fsm;

import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.StateShallowHistory;
import ca.uqac.lif.parkbench.Experiment;
import ca.uqac.lif.structures.MathList;

public class StateHistoryProvider extends CombinatorialTriagingFunctionProvider<MathList<Integer>>
{
	public StateHistoryProvider(AutomatonProvider provider, int strength)
	{
		super(provider, strength);
	}

	@Override
	public void write(Experiment e) 
	{
		super.write(e);
		e.setInput(CombinatorialTriagingFunctionProvider.FUNCTION, "State history");
	}

	@Override
	protected TriagingFunction<AtomicEvent, MathList<Integer>> instantiateFunction(Automaton aut)
	{
		return new StateShallowHistory(aut, m_strength);
	}

}
