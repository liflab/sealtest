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

/**
 * Hologram transformation that removes all the leaves of the evaluation
 * tree.
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class LeafDeletion<T extends Event> extends HologramTransformation<T> 
{
	/**
	 * If set to true, will delete atoms in addition to leaves
	 */
	protected boolean m_deleteAtoms = false;
	
	public LeafDeletion()
	{
		super();
	}
	
	protected LeafDeletion(boolean delete_atoms)
	{
		super();
		m_deleteAtoms = delete_atoms;
	}
	
	@Override
	public Operator<T> transform(Operator<T> tree) 
	{
		transformRecursive(tree);
		return tree;
	}
	
	protected void transformRecursive(Operator<T> node)
	{
		if (node.isDeleted())
		{
			return;
		}
		if (node instanceof EventLeaf || (m_deleteAtoms && node instanceof Atom))
		{
			node.delete();
			return;
		}

		List<Operator<T>> children = node.getTreeChildren();
		for (Operator<T> child : children)
		{
			// Recursively apply the transformation to children
			transformRecursive(child);
		}
	}
	
	@Override
	public String toString()
	{
		return "Leaf deletion";
	}
}
