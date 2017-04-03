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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.ecp.Event;

/**
 * Serialization of the internal state of a statechart. This includes
 * a pointer to its current (nested) state, and the current value of all
 * its state variables.
 *  
 * @author Sylvain Hallé
 *
 * @param <T> The type of events in the statechart
 */
public class Configuration<T extends Event> 
{
	/**
	 * The name of this state node
	 */
	protected final String m_name;
	
	/**
	 * The variable assignments of the statechart
	 */
	protected Map<String,Object> m_variables;
	
	/**
	 * Possible children of this state node
	 */
	protected final List<Configuration<T>> m_children;
	
	/**
	 * Creates a new configuration with a state of given name
	 * @param name The name of the state
	 */
	public Configuration(String name)
	{
		super();
		m_name = name;
		m_children = new ArrayList<Configuration<T>>();
		m_variables = null;
	}
	
	public Configuration(String name, Configuration<T> ... children)
	{
		this(name);
		for (Configuration<T> child : children)
		{
			m_children.add(child);
		}
	}
	
	public Configuration(String name, Map<String,Object> variables, Configuration<T> ... children)
	{
		this(name);
		for (Configuration<T> child : children)
		{
			m_children.add(child);
		}
		m_variables = new HashMap<String,Object>();
		m_variables.putAll(variables);
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
		if (m_variables != null && !m_variables.isEmpty())
		{
			out.append(m_variables);
		}
		return out.toString();
	}
	
	/**
	 * Sets the variable assignments of this configuration
	 * @param variables The variable assignments
	 */
	public void setVariables(Map<String,Object> variables)
	{
		if (variables == null)
		{
			m_variables = null;
		}
		else
		{
			m_variables = new HashMap<String,Object>();
			m_variables.putAll(variables);
		}
	}

	/**
	 * Adds a child node to this configuration
	 * @param child The child to add
	 */
	public void addChild(Configuration<T> child)
	{
		m_children.add(child);
	}
	
	/**
	 * Gets the list of children of this configuration
	 * @return The list of children
	 */
	public List<Configuration<T>> getChildren()
	{
		return m_children;
	}
	
	/**
	 * Gets the name of the state associated to this configuration node
	 * @return The name of the state
	 */
	public String getName() 
	{
		return m_name;
	}
	
	@Override
	public int hashCode()
	{
		if (m_variables == null)
			return 17 * m_name.hashCode() * m_children.size() + 37;
		return 17 * m_name.hashCode() * m_children.size() + 37 * m_variables.size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Configuration))
		{
			return false;
		}
		Configuration<?> n = (Configuration<?>) o;
		// Compare name
		if (n.m_name.compareTo(m_name) != 0)
			return false;
		// Compare children
		if (m_children.size() != n.m_children.size())
			return false;
		for (int i = 0; i < m_children.size(); i++)
		{
			if (!m_children.get(i).equals(n.m_children.get(i)))
				return false;
		}
		// Compare variable assignments
		if ((m_variables == null && n.m_variables != null) || (m_variables != null && n.m_variables == null))
		{
			return false;
		}
		if (m_variables != null && n.m_variables != null)
		{
			if (m_variables.size() != n.m_variables.size())
			{
				return false;
			}
			for (Map.Entry<String,Object> entry : m_variables.entrySet())
			{
				if (!n.m_variables.containsKey(entry.getKey()))
				{
					return false;
				}
				
			}
		}
		return true;
	}

	public static class UpStateNode<U extends Event> extends Configuration<U>
	{
		@SuppressWarnings("unchecked")
		public UpStateNode(Configuration<U> child)
		{
			super("..", child);
		}
	}

	/**
	 * Checks whether a configuration contains a state with given name
	 * @param stateName The name of the state to look for
	 * @return {@code true} if this state is found somewhere in, the
	 * configuration, {@code false} otherwise
	 */
	public boolean contains(String stateName)
	{
		if (m_name.compareTo(stateName) == 0)
		{
			return true;
		}
		for (Configuration<T> conf : m_children)
		{
			if (conf.contains(stateName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current configuration contains a variable of given name
	 * @param name The name of the variable to look for
	 * @return {@code true} if the variable is defined, {@code false}
	 * otherwise
	 */
	public boolean containsVariable(String name)
	{
		if (m_variables == null)
		{
			return false;
		}
		return m_variables.containsKey(name);
	}
	
	/**
	 * Retrieves the value of a state variable
	 * @param name The name of the variable
	 * @return The value of the variable, or {@code null} if the variable
	 * does not exist
	 */
	public Object getVariable(String name)
	{
		if (m_variables == null || !m_variables.containsKey(name))
		{
			return null;
		}
		return m_variables.get(name);
	}
}
