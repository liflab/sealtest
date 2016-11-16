package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class GraphvizHologramRenderer<T extends Event> extends HologramVisitor<T> 
{
	protected int m_parent = -1;
	
	protected StringBuilder m_buffer = new StringBuilder();

	@Override
	public void visit(Operator<T> op, int count) 
	{
		if (m_parent == -1)
		{
			m_buffer.append("digraph G {\n");
		}
		m_parent = count;
		m_buffer.append(count).append(" [label=\"").append(op.getRootSymbol()).append("\"];\n");
		if (m_parent != -1)
		{
			m_buffer.append(m_parent).append(" -> ").append(count).append(";\n");
		}
	}
	
	@Override
	public void backtrack(int count)
	{
		m_parent = count;
	}
	
	public String toDot()
	{
		return m_buffer.toString() + "}";
	}

}
