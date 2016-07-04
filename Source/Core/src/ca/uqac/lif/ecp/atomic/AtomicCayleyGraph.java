package ca.uqac.lif.ecp.atomic;

import ca.uqac.lif.ecp.CayleyGraph;

/**
 * Special case of Cayley graph where the triaging function operates
 * over atomic events
 * @param <U> The return type of the triaging function used to build the
 *   graph
 * @author Sylvain
 */
public class AtomicCayleyGraph<U> extends CayleyGraph<AtomicEvent,U>
{
	
	@Override
	public AtomicGraphPlotter<U> plotter()
	{
		return new AtomicGraphPlotter<U>(this);
	}
}
