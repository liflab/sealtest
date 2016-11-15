package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Provides a Cayley graph from a triaging function provider
 * @author Sylvain Hallé
 *
 * @param <T>
 * @param <U>
 */
public class TriagingFunctionCayleyGraphProvider<T extends Event,U> implements CayleyGraphProvider<T,U>
{
	protected TriagingFunctionProvider<T,U> m_provider;
	
	public TriagingFunctionCayleyGraphProvider(TriagingFunctionProvider<T,U> provider)
	{
		super();
		m_provider = provider;
	}

	@Override
	public CayleyGraph<T,U> getCayleyGraph() 
	{
		TriagingFunction<T,U> function = m_provider.getFunction();
		CayleyGraph<T,U> graph = function.getCayleyGraph();
		return graph;
	}

	@Override
	public void write(Experiment e) 
	{
		m_provider.write(e);
	}
}
