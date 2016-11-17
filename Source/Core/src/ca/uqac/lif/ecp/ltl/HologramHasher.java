package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class HologramHasher<T extends Event> extends HologramVisitor<T>
{
	StringBuilder m_buffer;
	
	boolean m_noChild = false;
	
	public HologramHasher()
	{
		super();
		m_buffer = new StringBuilder();
	}
	
	public String getHash(Operator<T> op)
	{
		HologramHasher<T> h = new HologramHasher<T>();
		op.acceptPrefix(h, true);
		return h.m_buffer.toString();
	}

	@Override
	public void visit(Operator<T> op, int count) 
	{
		m_noChild = false;
		m_buffer.append(op.getRootSymbol());
		if (!op.getTreeChildren().isEmpty())
		{
			m_buffer.append("\u2193"); // Down arrow
			m_noChild = true;
		}
	}
	
	@Override
	public void backtrack(int count)
	{
		if (m_noChild == false)
		{
			m_buffer.append("\u2191"); // Up arrow
		}
		m_noChild = false;
	}
}
