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

import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Applies to classes that can generate an automaton
 */
public interface AutomatonProvider
{
	public static final String PROPERTY_NAME = "property-name";
	
	public static final String PROPERTY_DESCRIPTION = "The name of the property represented by the automaton";
	
	/**
	 * Generates an automaton
	 * @return An automaton
	 */
	public Automaton getAutomaton();
	
	/**
	 * Writes additional data into an experiment
	 * @param e The experiment
	 */
	public void write(Experiment e);
}
