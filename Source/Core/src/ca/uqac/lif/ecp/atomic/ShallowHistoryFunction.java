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

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.structures.MathList;

public abstract class ShallowHistoryFunction<T> extends AutomatonFunction<MathList<T>> 
{
	protected FixedSizeWindow m_window;
	
	public ShallowHistoryFunction(Automaton a, int size)
	{
		super(a);
		m_window = new FixedSizeWindow(size);
	}
	
	/**
	 * Sliding window of elements of type T
	 */
	protected class FixedSizeWindow extends MathList<T>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * The size of the window
		 */
		protected int m_size;
		
		public FixedSizeWindow(int size)
		{
			super();
			m_size = size;
		}
		
		@Override
		public boolean add(T e)
		{
			if (size() == m_size)
			{
				remove(0);
			}
			return super.add(e);
		}
	}
	
	@Override
	public void reset()
	{
		super.reset();
		if (m_window != null)
		{
			m_window.clear();
		}
	}
	
	@Override
	public CayleyGraph<AtomicEvent,MathList<T>> getCayleyGraph()
	{
		AutomatonCayleyGraphFactory<MathList<T>> factory = new AutomatonCayleyGraphFactory<MathList<T>>(m_automaton.getAlphabet());
		return factory.getGraph(this);
	}
}
