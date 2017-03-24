/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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
package ca.uqac.lif.ecp.statechart;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;

public class StateNode<T extends Event> 
{
	/**
	 * The name of this state node
	 */
	protected final String m_name;
	
	/**
	 * Possible children of this state node
	 */
	protected final List<StateNode<T>> m_children;
	
	public StateNode(String name)
	{
		super();
		m_name = name;
		m_children = new ArrayList<StateNode<T>>();
	}
	
	public StateNode(String name, StateNode<T> ... children)
	{
		this(name);
		for (StateNode<T> child : children)
		{
			m_children.add(child);
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append(m_name);
		if (!m_children.isEmpty())
		{
			out.append(m_children.toString());
		}
		return out.toString();
	}

	
	public void addChild(StateNode<T> child)
	{
		m_children.add(child);
	}
	
	public List<StateNode<T>> getChildren()
	{
		return m_children;
	}
		
	public String getName() 
	{
		return m_name;
	}
	
	@Override
	public int hashCode()
	{
		return 17 * m_name.hashCode() * m_children.size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof StateNode))
		{
			return false;
		}
		StateNode<?> n = (StateNode<?>) o;
		if (n.m_name.compareTo(m_name) != 0)
			return false;
		if (m_children.size() != n.m_children.size())
			return false;
		for (int i = 0; i < m_children.size(); i++)
		{
			if (!m_children.get(i).equals(n.m_children.get(i)))
				return false;
		}
		return true;
	}

	public static class UpStateNode<U extends Event> extends StateNode<U>
	{
		@SuppressWarnings("unchecked")
		public UpStateNode(StateNode<U> child)
		{
			super("..", child);
		}
	}
}
