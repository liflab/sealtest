package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.CayleyGraphFactory;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.parkbench.Experiment;

public class FactoryCayleyGraphProvider<T extends Event,U> implements CayleyGraphProvider<T,U> 
{
	protected CayleyGraphFactory<T,U> m_factory;
	
	protected TriagingFunctionProvider<T,U> m_provider;
	
	public FactoryCayleyGraphProvider(CayleyGraphFactory<T,U> factory, TriagingFunctionProvider<T,U> provider)
	{
		super();
		m_factory = factory;
		m_provider = provider;
	}

	@Override
	public CayleyGraph<T, U> getCayleyGraph()
	{
		TriagingFunction<T,U> function = m_provider.getFunction();
		return m_factory.getGraph(function);
	}

	@Override
	public void write(Experiment e)
	{
		m_provider.write(e);
	}

}
