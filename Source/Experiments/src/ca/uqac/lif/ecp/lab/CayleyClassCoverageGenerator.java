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
import ca.uqac.lif.ecp.CayleyGraphTraceGenerator;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.SpanningTreeTraceGenerator;
import ca.uqac.lif.parkbench.Experiment;

public class CayleyClassCoverageGenerator<T extends Event,U> implements CayleyTraceGeneratorProvider<T,U>
{
	protected CayleyGraphProvider<T,U> m_provider;
	
	public CayleyClassCoverageGenerator(CayleyGraphProvider<T,U> provider)
	{
		super();
		m_provider = provider;
	}

	@Override
	public void write(Experiment e)
	{
		m_provider.write(e);
		e.write(TestSuiteGenerationExperiment.METHOD, "Cayley Graph ST");
	}
	
	@Override
	public CayleyGraphTraceGenerator<T,U> getGenerator() 
	{
		CayleyGraph<T,U> graph = m_provider.getCayleyGraph();
		SpanningTreeTraceGenerator<T,U> generator = new SpanningTreeTraceGenerator<T,U>(graph);
		return generator;
	}
}
