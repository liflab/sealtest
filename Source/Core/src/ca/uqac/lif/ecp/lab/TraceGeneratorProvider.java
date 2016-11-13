package ca.uqac.lif.ecp.lab;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TraceGenerator;
import ca.uqac.lif.parkbench.Experiment;

public interface TraceGeneratorProvider<T extends Event>
{
	/**
	 * Gets a trace generator
	 * @return The generator
	 */
	public TraceGenerator<T> getGenerator();
	
	/**
	 * Writes additional data into an experiment
	 * @param e The experiment
	 */
	public void write(Experiment e);
}
