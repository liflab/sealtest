package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class GraphvizHologramRenderer<T extends Event> extends HologramVisitor<T> 
{
	protected int m_parent = -1;
	
	protected StringBuilder m_buffer = new StringBuilder();

	@Override
	public void visit(Operator<T> op, int count) 
	{
		String color = getColor(op);
		if (m_parent == -1)
		{
			m_buffer.append("digraph G {\n node[shape=\"rectangle\",style=\"filled\"];\n");
		}
		m_buffer.append(" ").append(count).append(" [label=\"").append(op.getRootSymbol()).append("\",fillcolor=\"").append(color).append("\"];\n");
		if (m_parent != -1)
		{
			m_buffer.append(" ").append(m_parent).append(" -> ").append(count).append(";\n");
		}
		m_parent = count;
	}
	
	public void visit(T event, int count) 
	{
		if (m_parent == -1)
		{
			m_buffer.append("digraph G {\n");
		}
		m_buffer.append(" ").append(count).append(" [label=\"").append(event).append("\",shape=\"oval\"];\n");
		if (m_parent != -1)
		{
			m_buffer.append(" ").append(m_parent).append(" -> ").append(count).append(";\n");
		}
		m_parent = count;
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
	
	protected static String getColor(Operator<?> op)
	{
		Value v = op.getValue();
		if (v == Value.TRUE)
		{
			return "chartreuse";
		}
		if (v == Value.FALSE)
		{
			return "firebrick1";
		}
		return "white";
	}

}
