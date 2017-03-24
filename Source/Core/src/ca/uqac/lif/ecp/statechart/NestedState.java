package ca.uqac.lif.ecp.statechart;

import java.util.List;

import ca.uqac.lif.ecp.Event;

/**
 * A state that encloses multiple statecharts. Graphically, it is represented as a
 * box that contains other states and transitions. If the nested state contains
 * multiple statecharts, these are interpreted as <em>orthogonal regions</em>.
 * @author Sylvain Hallé
 */
public class NestedState<T extends Event> extends State
{
	/**
	 * The inner statecharts
	 */
	protected List<Statechart<T>> m_contents;
	
	/**
	 * Creates a new box state with a given name
	 * @param name The name
	 */
	public NestedState(String name)
	{
		super(name);
	}
	
	/**
	 * Creates a new box state with a given name and enclosed statechart
	 * @param name The name
	 * @param s The statecharts
	 */
	public NestedState(String name, Statechart<T> ... s)
	{
		super(name);
		for (Statechart<T> sc : s)
		{
			m_contents.add(sc);
		}
	}
	
	/**
	 * Sets the enclosed statechart that this box state contains
	 * @param s The statechart
	 * @return This box state
	 */
	public NestedState<T> addStatechart(Statechart<T> s)
	{
		m_contents.add(s);
		return this;
	}
	
	@Override
	public void reset()
	{
		for (Statechart<T> sc : m_contents)
		{
			sc.reset();
		}
	}
}
