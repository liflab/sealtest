package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;

/**
 * A state that encloses a statechart. Graphically, it is represented as a
 * box that contains other states and transitions.
 * @author Sylvain Hallé
 */
public class BoxState<T extends Event> extends State
{
	/**
	 * The inner statechart
	 */
	protected Statechart<T> m_contents;
	
	/**
	 * Creates a new box state with a given name
	 * @param name The name
	 */
	public BoxState(String name)
	{
		super(name);
	}
	
	/**
	 * Creates a new box state with a given name and enclosed statechart
	 * @param name The name
	 * @param s The statechart
	 */
	public BoxState(String name, Statechart<T> s)
	{
		super(name);
		m_contents = s;
	}
	
	/**
	 * Sets the enclosed statechart that this box state contains
	 * @param s The statechart
	 * @return This box state
	 */
	public BoxState<T> setStatechart(Statechart<T> s)
	{
		m_contents = s;
		return this;
	}
	
	@Override
	public void reset()
	{
		m_contents.reset();
	}
}
