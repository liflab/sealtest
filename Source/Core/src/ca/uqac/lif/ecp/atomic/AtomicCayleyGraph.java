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

import ca.uqac.lif.ecp.CayleyGraph;

/**
 * Special case of Cayley graph where the triaging function operates
 * over atomic events
 * @param <U> The return type of the triaging function used to build the
 *   graph
 * @author Sylvain
 */
public class AtomicCayleyGraph<U> extends CayleyGraph<AtomicEvent,U>
{
	
	@Override
	public AtomicGraphPlotter<U> plotter()
	{
		return new AtomicGraphPlotter<U>(this);
	}
}
