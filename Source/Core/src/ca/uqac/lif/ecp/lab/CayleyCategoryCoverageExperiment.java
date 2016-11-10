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

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.HypergraphTraceGenerator;
import ca.uqac.lif.ecp.TestSuite;

/**
 * Test suite generation experiment based on computing category
 * coverage on a Cayley graph.
 * 
 * @author Sylvain Hallé
 */
public abstract class CayleyCategoryCoverageExperiment<T extends Event,U> extends TestSuiteGenerationExperiment
{
	/**
	 * The Cayley graph used to generate the test suite
	 */
	CayleyGraph<T,U> m_graph;
	
	public CayleyCategoryCoverageExperiment()
	{
		super();
		m_graph = null;
	}
	
	public CayleyCategoryCoverageExperiment(CayleyGraph<T,U> graph)
	{
		super();
		m_graph = graph;
	}
	
	public void setGraph(CayleyGraph<T,U> graph)
	{
		m_graph = graph;
	}
	
	@Override
	public final TestSuite<T> getTestSuite() 
	{
		if (m_graph == null)
		{
			m_graph = getCayleyGraph();
		}
		HypergraphTraceGenerator<T,U> generator = new HypergraphTraceGenerator<T,U>(m_graph);
		TestSuite<T> suite = generator.generateTraces();
		return suite;
	}
	
	/**
	 * Gets the Cayley graph required for this experiment. This method is
	 * called if the experiment is not instantiated directly with an existing
	 * Cayley graph.
	 * @return The graph
	 */
	protected abstract CayleyGraph<T,U> getCayleyGraph();

}
