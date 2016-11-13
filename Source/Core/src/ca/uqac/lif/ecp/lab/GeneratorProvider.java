package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.ecp.TraceGenerator;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Generates a test suite based on a trace generator
 * @author Sylvain Hallé
 *
 * @param <T>
 */
public class GeneratorProvider<T extends Event> implements TestSuiteProvider<T> 
{
	protected TraceGeneratorProvider<T> m_provider;
	
	public GeneratorProvider(TraceGeneratorProvider<T> provider)
	{
		super();
		m_provider = provider;
	}

	@Override
	public TestSuite<T> getTestSuite() 
	{
		TraceGenerator<T> generator = m_provider.getGenerator();
		return generator.generateTraces();
	}

	@Override
	public void write(Experiment e)
	{
		m_provider.write(e);
	}
}
