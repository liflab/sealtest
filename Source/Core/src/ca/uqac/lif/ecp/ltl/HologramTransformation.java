package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public abstract class HologramTransformation<T extends Event>
{
	public abstract Operator<T> transform(Operator<T> tree);
}
