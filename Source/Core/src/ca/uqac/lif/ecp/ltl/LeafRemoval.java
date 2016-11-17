package ca.uqac.lif.ecp.ltl;

import java.util.List;

import ca.uqac.lif.ecp.Event;

public class LeafRemoval<T extends Event> extends HologramTransformation<T> 
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
		if (node instanceof EventLeaf)
		{
			node.delete();
		}
	}
}
