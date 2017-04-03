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

/**
 * A state that encloses multiple statecharts. Graphically, it is represented as a
 * box that contains other states and transitions. If the nested state contains
 * multiple statecharts, these are interpreted as <em>orthogonal regions</em>.
 * @author Sylvain Hallé
 */
public class NestedState<T extends Event> extends State<T>
{
	/**
	 * The inner statecharts
	 */
	private List<Statechart<T>> m_contents;
	
	/**
	 * Creates a new box state with a given name
	 * @param name The name
	 */
	public NestedState(String name)
	{
		super(name);
		setContents(new ArrayList<Statechart<T>>());
	}
	
	/**
	 * Creates a new box state with a given name and a predefined ID
	 * @param name The name
	 * @param id The ID
	 */
	protected NestedState(String name, int id)
	{
		super(name, id);
		setContents(new ArrayList<Statechart<T>>());
	}
	
	/**
	 * Creates a new box state with a given name and enclosed statechart
	 * @param name The name
	 * @param s The statecharts
	 */
	public NestedState(String name, Statechart<T> ... s)
	{
		super(name);
		setContents(new ArrayList<Statechart<T>>());
		for (Statechart<T> sc : s)
		{
			getContents().add(sc);
		}
	}
	
	/**
	 * Sets the enclosed statechart that this box state contains
	 * @param s The statechart
	 * @return This box state
	 */
	public NestedState<T> addStatechart(Statechart<T> s)
	{
		getContents().add(s);
		return this;
	}
	
	@Override
	public void reset()
	{
		for (Statechart<T> sc : getContents())
		{
			sc.reset();
		}
	}
	
	@Override
	public NestedState<T> clone(Statechart<T> parent)
	{
		NestedState<T> s = new NestedState<T>(m_name, m_id);
		for (Statechart<T> sc : getContents())
		{
			s.getContents().add(sc.clone(parent));
		}
		return s;
	}

	/**
	 * @return the m_contents
	 */
	public List<Statechart<T>> getContents() {
		return m_contents;
	}

	/**
	 * @param m_contents the m_contents to set
	 */
	public void setContents(List<Statechart<T>> m_contents) {
		this.m_contents = m_contents;
	}
}
