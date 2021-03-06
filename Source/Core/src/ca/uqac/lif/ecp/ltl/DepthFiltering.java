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

import java.util.List;

import ca.uqac.lif.ecp.Event;

public class DepthFiltering<T extends Event> extends HologramTransformation<T> 
{
	/**
	 * The depth at which to cut the tree
	 */
	protected transient int m_depth = 1; 
	
	public DepthFiltering(int depth)
	{
		super();
		m_depth = depth;
	}

	@Override
	public Operator<T> transform(Operator<T> tree)
	{
		transformRecursive(tree, 0);
		return tree;
	}
	
	protected void transformRecursive(Operator<T> node, int depth)
	{
		if (node.isDeleted())
		{
			return;
		}
		if (depth >= m_depth)
		{
			node.delete();
			return;
		}
		List<Operator<T>> children = node.getTreeChildren();
		for (Operator<T> child : children)
		{
			// Recursively apply the transformation to children
			transformRecursive(child, depth + 1);
		}
	}
	
	@Override
	public String toString()
	{
		return "Filtering of depth " + m_depth;
	}
}
