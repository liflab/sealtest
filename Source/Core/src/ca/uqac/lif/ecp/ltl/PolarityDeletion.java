package ca.uqac.lif.ecp.ltl;

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class PolarityDeletion<T extends Event> extends HologramTransformation<T> 
{

	@Override
	public Operator<T> transform(Operator<T> tree) 
	{
		transformRecursive(tree);
		return tree;
	}
	
	protected void transformRecursive(Operator<T> node)
	{
		if (node.isDeleted())
		{
			return;
		}
		List<Operator<T>> children = node.getTreeChildren();
		for (Operator<T> child : children)
		{
			// Recursively apply the transformation to children
			transformRecursive(child);
		}
		// In a false "and" or "G", remove all children that are not false
		if (node.getValue() == Value.FALSE && (node instanceof And || node instanceof Globally))
		{
			for (Operator<T> child : children)
			{
				if (child.getValue() != Value.FALSE)
				{
					child.delete();
				}
			}
		}
		// In a true "or" or "F", remove all children that are not true
		if (node.getValue() == Value.TRUE && (node instanceof Or || node instanceof Eventually))
		{
			for (Operator<T> child : children)
			{
				if (child.getValue() != Value.TRUE)
				{
					child.delete();
				}
			}
		}
	}
}
