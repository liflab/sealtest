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
package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.ecp.lab.TriagingFunctionProvider;
import ca.uqac.lif.ecp.ltl.HologramFunction;
import ca.uqac.lif.ecp.ltl.HologramTransformation;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.parkbench.Experiment;

public class HologramTriagingFunctionProvider<T extends Event> implements TriagingFunctionProvider<T,Operator<T>> 
{
	protected HologramTransformationProvider<T> m_transformationProvider;
	
	protected OperatorProvider<T> m_operatorProvider;
	
	public HologramTriagingFunctionProvider(OperatorProvider<T> op, HologramTransformationProvider<T> hp)
	{
		super();
		m_operatorProvider = op;
		m_transformationProvider = hp;
	}
	
	@Override
	public TriagingFunction<T, Operator<T>> getFunction()
	{
		HologramTransformation<T> transformation = m_transformationProvider.getTransformation();
		Operator<T> operator = m_operatorProvider.getOperator();
		HologramFunction<T> function = new HologramFunction<T>(operator, transformation);
		return function;
	}

	@Override
	public void write(Experiment e) 
	{
		m_transformationProvider.write(e);
		m_operatorProvider.write(e);
	}
}
