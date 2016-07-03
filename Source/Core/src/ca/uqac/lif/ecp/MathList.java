package ca.uqac.lif.ecp;

import java.util.ArrayList;

/**
 * List implementation that behaves like a real list, i.e. two lists
 * are equal if and only if they contain the same elements in the
 * same order.
 *
 * @param <T> The type of the lists's elements
 */
public class MathList<T> extends ArrayList<T> 
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
		if (o == null || !(o instanceof MathList))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		MathList<T> h = (MathList<T>) o;
		if (h.size() != size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			T e1 = get(i);
			T e2 = h.get(i);
			if (!e1.equals(e2))
			{
				return false;
			}
		}
		return true;
	}
}
