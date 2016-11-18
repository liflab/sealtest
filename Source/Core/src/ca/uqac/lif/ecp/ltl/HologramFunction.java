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

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.structures.MathSet;

public class HologramFunction<T extends Event> extends TriagingFunction<T, Operator<T>> 
{
	/**
	 * The transformation to apply to the hologram
	 */
	protected HologramTransformation<T> m_transformation;
	
	/**
	 * The LTL formula on which this function operates
	 */
	protected Operator<T> m_formula;
	
	/**
	 * Creates a new hologram triaging function
	 * @param formula The formula formula on which this function operates
	 * @param transformation The transformation to apply to the hologram
	 */
	public HologramFunction(Operator<T> formula, HologramTransformation<T> transformation)
	{
		super();
		m_formula = formula;
		m_transformation = transformation;
	}
	
	/**
	 * Creates a new hologram triaging function
	 * @param formula The formula formula on which this function operates
	 */
	public HologramFunction(Operator<T> formula)
	{
		this(formula, new IdentityHologramTransformation<T>());
	}
	
	/**
	 * Gets the equivalence class associated with a given trace
	 * @param The trace to read
	 * @return The equivalence class
	 */
	@Override
	public MathSet<Operator<T>> getClass(Trace<T> trace)
	{
		m_formula.clear();
		for (T event : trace)
		{
			m_formula.evaluate(event);
		}
		Operator<T> tree = m_formula.copy(true);
		Operator<T> hologram = m_transformation.transform(tree);
		hologram.evaluate(); // Refresh truth value. Currently turned off since causes null pointers
		return new MathSet<Operator<T>>(hologram);
	}

	@Override
	public MathSet<Operator<T>> read(T event) 
	{
		// This method is not called by hologram functions
		return null;
	}
	
	/**
	 * Gets the formula on which this function operates
	 * @return The formula
	 */
	public Operator<T> getFormula()
	{
		return m_formula;
	}

	@Override
	public MathSet<Operator<T>> getStartClass() 
	{
		return new MathSet<Operator<T>>(m_formula.copy(true));
	}
	
	@Override
	public void reset()
	{
		m_formula.clear();
	}

	@Override
	public CayleyGraph<T, Operator<T>> getCayleyGraph() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
