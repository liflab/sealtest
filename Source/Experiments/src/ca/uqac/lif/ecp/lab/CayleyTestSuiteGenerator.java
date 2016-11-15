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
package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraphTraceGenerator;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Generates a test suite from a Cayley graph and a Cayley graph
 * trace generator.
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class CayleyTestSuiteGenerator<T extends Event,U> implements TestSuiteProvider<T>
{
	/**
	 * The generator provider to obtain a Cayley graph trace generator
	 */
	protected CayleyTraceGeneratorProvider<T,U> m_generatorProvider;
	
	/**
	 * Creates a new test suite provider
	 * @param graph_provider The graph provider to obtain the Cayley graph
	 * @param generator_provider The generator provider to obtain a Cayley graph
	 *    trace generator
	 */
	public CayleyTestSuiteGenerator(CayleyTraceGeneratorProvider<T,U> generator_provider)
	{
		super();
		m_generatorProvider = generator_provider;
	}
	
	@Override
	public TestSuite<T> getTestSuite() 
	{
		CayleyGraphTraceGenerator<T,U> generator = m_generatorProvider.getGenerator();
		return generator.generateTraces();
	}

	@Override
	public void write(Experiment e)
	{
		m_generatorProvider.write(e);
	}
}
