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

import ca.uqac.lif.ecp.Event;

/**
 * Hologram transformation that does nothing
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class IdentityHologramTransformation<T extends Event> extends HologramTransformation<T>
{
	public Operator<T> transform(Operator<T> tree)
	{
		return tree;
	}
	
	@Override
	public String toString()
	{
		return "Identity";
	}
}
