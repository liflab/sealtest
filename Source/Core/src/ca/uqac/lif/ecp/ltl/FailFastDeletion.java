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
 * Hologram transformation that deletes all children of a node up to
 * the first node that determines its truth value. This supposes that
 * tree children are ordered.
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class FailFastDeletion<T extends Event> extends HologramTransformation<T> 
{

	@Override
	public Operator<T> transform(Operator<T> tree) 
	{
		transformRecursive(tree, true);
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
				if (first_seen)
				{
					// Once we've seen the first false child, delete everything
					child.delete();
					continue;
				}
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
				if (first_seen)
				{
					// Once we've seen the first false child, delete everything
					child.delete();
					continue;
				}
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
		// The case of -> requires special attention
		// Note that there is no difference between fail-fast and polarity
		// for this special case
		if (node instanceof Implies && node.getValue() == Value.TRUE)
		{
			Operator<T> left = children.get(0);
			Operator<T> right = children.get(1);
			if (left.getValue() == Value.FALSE)
			{
				right.delete();
			}
			else if (right.getValue() == Value.TRUE)
			{
				// left is true, but since right is true,
				// the implication would be true no matter what
				// value left has
				left.delete();
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "Fail-fast deletion";
	}
}
