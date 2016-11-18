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
import ca.uqac.lif.ecp.ltl.Operator.Value;

/**
 * Hologram transformation that deletes all children of the root node
 * past a specific count.
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class RootChildDeletion<T extends Event> extends HologramTransformation<T> 
{
	/**
	 * The maximum number of children that the root node can have
	 */
	protected int m_maxChildren = 1;
	
	public RootChildDeletion(int max_children)
	{
		super();
		m_maxChildren = max_children;
	}

	@Override
	public Operator<T> transform(Operator<T> tree) 
	{
		List<Operator<T>> children = tree.getTreeChildren();
		int n = 0;
		for (Operator<T> child : children)
		{
			if (child.isDeleted())
			{
				// Ignore deleted nodes
				continue;
			}
			n++;
			if (n > m_maxChildren)
			{
				child.delete();
			}
		}
		return tree;
	}
	
	protected void transformRecursive(Operator<T> node, boolean fail_fast)
	{
		if (node.isDeleted())
		{
			return;
		}
		List<Operator<T>> children = node.getTreeChildren();
		for (Operator<T> child : children)
		{
			// Recursively apply the transformation to children
			transformRecursive(child, fail_fast);
		}
		// In a false "and" or "G", remove all children that are not false
		boolean first_seen = false;
		if (node.getValue() == Value.FALSE && (node instanceof And || node instanceof Globally))
		{
			for (Operator<T> child : children)
			{
				if (child.getValue() != Value.FALSE)
				{
					if (!fail_fast || first_seen)
					{
						child.delete();
					}
				}
				else
				{
					first_seen = true;
				}
			}
		}
		// In a true "or" or "F", remove all children that are not true
		first_seen = false;
		if (node.getValue() == Value.TRUE && (node instanceof Or || node instanceof Eventually))
		{
			for (Operator<T> child : children)
			{
				if (child.getValue() != Value.TRUE)
				{
					if (!fail_fast || first_seen)
					{
						child.delete();
					}
				}
				else
				{
					first_seen = true;
				}
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "Keep only the first " + m_maxChildren + " children";
	}
}
