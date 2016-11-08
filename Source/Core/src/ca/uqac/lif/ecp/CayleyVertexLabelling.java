/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hall�

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
package ca.uqac.lif.ecp;

import ca.uqac.lif.ecp.graphs.VertexLabelling;
import ca.uqac.lif.structures.MathSet;

/**
 * Labelling of a Cayley Graph. This is a mapping from a vertex ID (integer)
 * to a <em>set</em> of objects of type <code>V</code>.
 * @author Sylvain Hallé
 *
 * @param <U> The type of the categories
 */
public class CayleyVertexLabelling<U extends Object> extends VertexLabelling<MathSet<U>>
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

}
