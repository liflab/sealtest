package ca.uqac.lif.ecp;

import org.junit.Test;

public class IntegerAtom extends Event
{
	private final int x;
	
	public IntegerAtom(int x)
	{
		super();
		this.x = x;
	}
	
	@Test
	public void dummyTest()
	{
		
	}
	
	@Override
	public int hashCode()
	{
		return x;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof IntegerAtom && ((IntegerAtom) o).x == this.x;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(x);
	}
}
