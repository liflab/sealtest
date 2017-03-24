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
package ca.uqac.lif.ecp.statechart;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

public abstract class ShallowHistoryFunction<T extends Event,U> extends StatechartFunction<T,MathList<U>> 
{
	protected FixedSizeWindow m_window = null;
	
	public ShallowHistoryFunction(Statechart<T> a, int size)
	{
		super(a);
		m_window = new FixedSizeWindow(size);
	}
	
	/**
	 * Sliding window of elements of type U
	 */
	protected class FixedSizeWindow extends MathList<U>
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
		public boolean add(U e)
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
}
