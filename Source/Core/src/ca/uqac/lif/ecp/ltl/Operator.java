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
 * Top-level class for all LTL connectives and operators
 * @param <T> The type of the events 
 * @author Sylvain Hallé
 */
public abstract class Operator<T extends Event>
{
	/**
	 * Truth value
	 */
	public enum Value {TRUE, FALSE, INCONCLUSIVE};
	
	/**
	 * The current value that this tree holds
	 */
	protected Value m_value = Value.INCONCLUSIVE;
	
	/**
	 * Whether this node is deleted from a hologram
	 */
	protected boolean m_deleted = false;
	
	/**
	 * Gets the current truth value of the operator
	 * @return The current truth value of the operator
	 */
	public final Value getValue()
	{
		return m_value;
	}
	
	/**
	 * Creates a deep copy of this operator
	 * @return The copy
	 */
	public final Operator<T> copy()
	{
		return copy(false);
	}
	
	/**
	 * Copies the internal content of this operator into a new instance
	 * @param o The new instance
	 * @param with_tree Set to <code>true</code> to also copy data related
	 *   to the operator's evaluation tree
	 */
	protected void copyInto(Operator<T> o, boolean with_tree)
	{
		if (with_tree)
		{
			o.m_value = m_value;
			o.m_deleted = m_deleted;
		}
	}
	
	/**
	 * Determines if a node is deleted
	 * @return true if the node is deleted
	 */
	public final boolean isDeleted()
	{
		return m_deleted;
	}
	
	/**
	 * Gets the size of this formula
	 * @return The size
	 */
	public final int size()
	{
		return size(false);
	}
	
	/**
	 * Creates a deep copy of this operator
	 * @param with_tree If set to <code>true</code>, will also copy the
	 *   evaluation tree
	 * @return The copy
	 */
	public abstract Operator<T> copy(boolean with_tree);
	
	/**
	 * Evaluates the operator on the new event
	 * @param event The event
	 */
	public abstract void evaluate(T event);
	
	/**
	 * Refreshes the evaluation of a formula, without sending any new event.
	 * This is useful to make sure the truth labelling of a hologram is
	 * correct after performing some deletions.
	 */
	public final void evaluate()
	{
		evaluate(null);
	}
	
	/**
	 * Marks a node as deleted
	 */
	public abstract void delete();
	
	/**
	 * Gets the size of this formula
	 * @param with_tree If set to <code>true</code>, will compute
	 *  the size of the evaluation tree of this formula instead
	 * @return The size
	 */
	public abstract int size(boolean with_tree);
	
	/**
	 * Gets the symbol of the operator for the root of the evaluation tree
	 * @return The symbol
	 */
	public abstract String getRootSymbol();
	
	/**
	 * Accepts a hologram visitor into the operator
	 * @param visitor The visitor
	 */
	public final void acceptPrefix(HologramVisitor<T> visitor)
	{
		acceptPrefix(visitor, false);
	}
	
	/**
	 * Accepts a hologram visitor into the operator
	 * @param visitor The visitor
	 */
	public final void acceptPostfix(HologramVisitor<T> visitor)
	{
		acceptPostfix(visitor, false);
	}
	
	/**
	 * Accepts a hologram visitor into the evaluation tree for this
	 * operator
	 * @param visitor The visitor
	 * @param in_tree Whether to visit the evaluation tree (true) or
	 *   the formula (false)
	 */
	public abstract void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree);
	
	/**
	 * Accepts a hologram visitor into the evaluation tree for this
	 * operator
	 * @param visitor The visitor
	 * @param in_tree Whether to visit the evaluation tree (true) or
	 *   the formula (false)
	 */
	public abstract void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree);
	
	/**
	 * Gets the ordered list of the children in the evaluation tree for this
	 * operator
	 * @return The children
	 */
	public abstract List<Operator<T>> getTreeChildren();
	
	/**
	 * Adds an operand to this operator
	 * @param op The operand
	 */
	public abstract void addOperand(Operator<T> op);
	
	/**
	 * Clears the state of this operator
	 */
	public void clear()
	{
		m_value = Value.INCONCLUSIVE;
	}
	
	/**
	 * Cleans the deleted nodes of the evaluation tree
	 */
	public abstract void clean();
}
