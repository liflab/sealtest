package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class PolarityDeletion<T extends Event> extends FailFastDeletion<T> 
{
	@Override
	public Operator<T> transform(Operator<T> tree) 
	{
		transformRecursive(tree, false);
		return tree;
	}
}
