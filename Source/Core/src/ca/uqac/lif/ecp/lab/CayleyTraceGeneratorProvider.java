package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.CayleyGraphTraceGenerator;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.parkbench.Experiment;

public interface CayleyTraceGeneratorProvider<T extends Event,U>
{
	public CayleyGraphTraceGenerator<T,U> getGenerator();
	
	public void write(Experiment e);
}
