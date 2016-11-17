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

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.graphs.Vertex;

/**
 * Cayley graph factory whose triaging function is a based on a
 * propositional LTL formula
 * 
 * @author Sylvain Hallé
 */
public class AtomicLtlCayleyGraphFactory extends LtlCayleyGraphFactory<AtomicEvent>
{
	/**
	 * The set of atomic events that will be used as the alphabet
	 */
	protected Set<AtomicEvent> m_possibleEvents;
	
	AtomicLtlCayleyGraphFactory()
	{
		super();
		m_possibleEvents = null;
	}
	
	public CayleyGraph<AtomicEvent,Operator<AtomicEvent>> getGraph(HologramFunction<AtomicEvent> f)
	{
		// Before launching the algorithm, we collect all the atoms
		// occurring in the formula and take that as our alphabet
		Operator<AtomicEvent> op = f.getFormula();
		AtomCollector ac = new AtomCollector();
		op.acceptPostfix(ac);
		m_possibleEvents = ac.getEvents();
		return super.getGraph(f);
	}

	@Override
	protected Set<AtomicEvent> getNextEvents(Vertex<AtomicEvent> vertex) 
	{
		return m_possibleEvents;
	}
	
	/**
	 * Visitor that collects all the atoms occurring in the formula
	 */
	protected static class AtomCollector extends HologramVisitor<AtomicEvent>
	{
		protected Set<AtomicEvent> m_collectedEvents;
		
		public AtomCollector()
		{
			super();
			m_collectedEvents = new HashSet<AtomicEvent>();
		}

		@Override
		public void visit(Operator<AtomicEvent> op, int count)
		{
			if (op instanceof Atom)
			{
				Atom<AtomicEvent> a = (Atom<AtomicEvent>) op;
				m_collectedEvents.add(a.getEvent());
			}
		}
		
		public Set<AtomicEvent> getEvents()
		{
			return m_collectedEvents;
		}
	}
}
