package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.DepthFiltering;
import ca.uqac.lif.ecp.ltl.FailFastDeletion;
import ca.uqac.lif.ecp.ltl.HologramComposition;
import ca.uqac.lif.ecp.ltl.HologramTransformation;
import ca.uqac.lif.ecp.ltl.IdentityHologramTransformation;
import ca.uqac.lif.ecp.ltl.LeafDeletion;
import ca.uqac.lif.ecp.ltl.PolarityDeletion;
import ca.uqac.lif.ecp.ltl.RootChildDeletion;
import ca.uqac.lif.parkbench.Experiment;

/**
 * Provides a hologram transformation based on a string that describes it
 * @author Sylvain Hallé
 */
public class StringTransformationProvider extends HologramTransformationProvider<AtomicEvent>
{
	protected HologramTransformation<AtomicEvent> m_transformation;
	
	public StringTransformationProvider(String label)
	{
		super();
		m_transformation = getTransformation(label);
	}

	@Override
	public HologramTransformation<AtomicEvent> getTransformation()
	{
		return m_transformation;
	}

	@Override
	public void write(Experiment e) 
	{
		super.write(e);
		e.setInput(TRANSFORMATION, m_transformation.toString());
	}
	
	public static HologramTransformation<AtomicEvent> getTransformation(String transformation)
	{
		String[] parts = transformation.split("\\s*\\+\\s*");
		if (parts.length == 1)
		{
			return convertTransformation(parts[0]);
		}
		HologramComposition<AtomicEvent> comp = new HologramComposition<AtomicEvent>();
		for (String trans : parts)
		{
			HologramTransformation<AtomicEvent> t = convertTransformation(trans);
			comp.add(t);
		}
		return comp;
	}
	
	protected static HologramTransformation<AtomicEvent> convertTransformation(String transformation)
	{
		if (transformation.compareToIgnoreCase("FFD") == 0)
		{
			return new FailFastDeletion<AtomicEvent>();
		}
		else if (transformation.compareToIgnoreCase("LD") == 0)
		{
			return new LeafDeletion<AtomicEvent>();
		}
		else if (transformation.compareToIgnoreCase("PD") == 0)
		{
			return new PolarityDeletion<AtomicEvent>();
		}
		else if (transformation.toUpperCase().startsWith("DF"))
		{
			int n = Integer.parseInt(transformation.substring(2).trim());
			return new DepthFiltering<AtomicEvent>(n);
		}
		else if (transformation.toUpperCase().startsWith("RC"))
		{
			int n = Integer.parseInt(transformation.substring(2).trim());
			return new RootChildDeletion<AtomicEvent>(n);
		}
		return new IdentityHologramTransformation<AtomicEvent>();	
	}

}
