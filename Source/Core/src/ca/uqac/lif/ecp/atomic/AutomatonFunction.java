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
package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.Vertex;

/**
 * Triaging function based on a finite-state automaton
 */
public abstract class AutomatonFunction<U extends Object> extends TriagingFunction<AtomicEvent,U>
{
	protected Automaton m_automaton;
	
	protected Vertex<AtomicEvent> m_currentVertex;
	
	public AutomatonFunction(Automaton a)
	{
		super();
		m_automaton = a;
		reset();
	}
	
	@Override
	public void reset()
	{
		m_currentVertex = m_automaton.getInitialVertex(); 
	}
	
	@Override
	public U read(AtomicEvent e)
	{
		Edge<AtomicEvent> edge = m_automaton.getTransition(m_currentVertex, e);
		m_currentVertex = m_automaton.getVertex(edge.getDestination());
		assert m_currentVertex != null;
		return processTransition(edge);
	}
	
	public abstract U processTransition(Edge<AtomicEvent> edge);
}
