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

	public static class UpStateNode<U extends Event> extends StateNode<U>
	{
		@SuppressWarnings("unchecked")
		public UpStateNode(StateNode<U> child)
		{
			super("..", child);
		}
	}
}
