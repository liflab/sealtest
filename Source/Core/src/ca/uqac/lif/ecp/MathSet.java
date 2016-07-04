package ca.uqac.lif.ecp;

import java.util.HashSet;

/**
 * Set implementation that behaves like a real set, i.e. two sets
 * are equal if and only if they contain the same elements.
 *
 * @param <T> The type of the set's elements
 */
public class MathSet<T> extends HashSet<T> 
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode()
	{
		return size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof MathSet))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		MathSet<T> h = (MathSet<T>) o;
		if (h.size() != size())
		{
			return false;
		}
		for (T st : this)
		{
			if (!h.contains(st))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("{");
		boolean first = true;
		for (T e : this)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.append(",");
			}
			out.append(e);
		}
		out.append("}");
		return out.toString();
	}
}
