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
import ca.uqac.lif.ecp.lab.TriagingFunctionProvider;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Provides a triaging function from an automaton
 * @author Sylvain Hallé
 *
 * @param <U> The type of the categories in the triaging function
 */
public abstract class AutomatonTriagingFunctionProvider<U> implements TriagingFunctionProvider<AtomicEvent,U>
{
	/**
	 * The provider to obtain an automaton
	 */
	AutomatonProvider m_provider;
	
	/**
	 * Creates a new triaging function provider 
	 * @param provider The provider to obtain an automaton
	 */
	public AutomatonTriagingFunctionProvider(AutomatonProvider provider)
	{
		super();
		m_provider = provider;
	}

	@Override
	public final TriagingFunction<AtomicEvent,U> getFunction()
	{
		return instantiateFunction(m_provider.getAutomaton());
	}
	
	@Override
	public void write(Experiment e) 
	{
		m_provider.write(e);
	}
	
	/**
	 * Produces an instance of the triaging function based on the
	 * automaton
	 * @param aut The automaton
	 * @return The triaging function
	 */
	protected abstract TriagingFunction<AtomicEvent,U> instantiateFunction(Automaton aut);

}
