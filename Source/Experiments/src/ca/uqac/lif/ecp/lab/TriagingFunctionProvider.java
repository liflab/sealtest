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

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Applies to classes that can generate a triaging function
 */
public interface TriagingFunctionProvider<T extends Event,U>
{
	/**
	 * Generates a triaging function
	 * @return A function
	 */
	public abstract TriagingFunction<T,U> getFunction();
	
	/**
	 * Writes additional data into an experiment
	 * @param e The experiment
	 */
	public void write(Experiment e);
}
