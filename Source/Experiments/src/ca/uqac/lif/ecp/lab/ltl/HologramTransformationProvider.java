package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.HologramTransformation;
import ca.uqac.lif.parkbench.Experiment;

public abstract class HologramTransformationProvider<T extends Event>
{
	public static final transient String TRANSFORMATION = "Transformation";
	
	public abstract HologramTransformation<T> getTransformation();
	
	public void write(Experiment e)
	{
		e.describe(TRANSFORMATION, "Transformation applied on the evaluation tree");		
	}
}
