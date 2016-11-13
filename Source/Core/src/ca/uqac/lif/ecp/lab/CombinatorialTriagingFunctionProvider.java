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
package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.parkbench.Experiment;

public abstract class CombinatorialTriagingFunctionProvider<U> extends AutomatonTriagingFunctionProvider<U>
{
	/**
	 * The strength of the experiment
	 */
	protected int m_strength;
	
	/**
	 * Creates a new t-way function provider
	 * @param provider The provider to get the automaton this function will use
	 * @param strength The strength of the function (i.e. the value of <i>t</i>)
	 */
	public CombinatorialTriagingFunctionProvider(AutomatonProvider provider, int strength)
	{
		super(provider);
		m_strength = strength;
	}
	
	@Override
	public void write(Experiment e) 
	{
		super.write(e);
		e.describe("strength", "The strength of the experiment");
		e.setInput("strength", m_strength);
		e.describe("function", "The triaging function used");
	}
}
