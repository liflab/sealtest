package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

/**
 * Hologram transformation that does nothing
 * 
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class IdentityHologramTransformation<T extends Event> extends HologramTransformation<T>
{
	public Operator<T> transform(Operator<T> tree)
	{
		return tree;
	}
}
