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

import ca.uqac.lif.ecp.Event;

/**
 * Implementation of the "Release" temporal operator.
 * When writing &psi; <b>R</b> &phi;, &phi; has to be true until and including 
 * the point where &psi; first becomes true; if &psi; 
 * never becomes true, &phi; must remain true forever.
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class Release<T extends Event> extends NaryOperator<T> 
{	
	public Release()
	{
		super("U");
	}
	
	public Release(Object ... ops)
	{
		super("U", ops);
	}

	@Override
	public void evaluate(T event)
	{
		Operator<T> left = m_operands.get(0);
		Operator<T> right = m_operands.get(1);
		if (event != null)
		{
			left.evaluate(event);
			right.evaluate(event);
		}
		if (m_value == Value.INCONCLUSIVE)
		{
			Value l_value = left.getValue();
			Value r_value = right.getValue();
			if (r_value == Value.FALSE)
			{
				m_value = Value.FALSE;
			}
			else if (l_value == Value.TRUE)
			{
				if (r_value == Value.FALSE)
				{
					m_value = Value.FALSE;
				}
				else
				{
					m_value = Value.TRUE;
				}
			}
		}
	}
	
	@Override
	public Release<T> copy(boolean with_tree)
	{
		Release<T> u = new Release<T>();
		super.copyInto(u, with_tree);
		u.m_value = m_value;
		return u;
	}

}
