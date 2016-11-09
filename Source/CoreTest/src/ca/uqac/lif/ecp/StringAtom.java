package ca.uqac.lif.ecp;

import org.junit.Test;

public class StringAtom extends Event
{
	private final String x;
	
	public StringAtom(String x)
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
		return x.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o != null && o instanceof StringAtom && ((StringAtom) o).x.compareTo(this.x) == 0;
	}
	
	@Override
	public String toString()
	{
		return "\"" + x + "\"";
	}
}