package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.HologramTransformation;
import ca.uqac.lif.parkbench.Experiment;

public class DirectHologramTransformationProvider<T extends Event> extends HologramTransformationProvider<T> 
{
	protected final HologramTransformation<T> m_transformation;
	
	public DirectHologramTransformationProvider(HologramTransformation<T> trans)
	{
		super();
		m_transformation = trans;
	}

	@Override
	public HologramTransformation<T> getTransformation() 
	{
		return m_transformation;
	}

	@Override
	public void write(Experiment e)
	{
		super.write(e);
		e.setInput(TRANSFORMATION, m_transformation.toString());
	}

}
