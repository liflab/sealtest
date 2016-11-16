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
		if (m_parent == -1)
		{
			m_buffer.append("digraph G {\n node[shape=\"rectangle\",style=\"filled\"];\n");
		}
		m_buffer.append(" ").append(count).append(" [label=\"").append(op.getRootSymbol()).append("\",").append(getVertexStyle(op)).append("];\n");
		if (m_parent != -1)
		{
			m_buffer.append(" ").append(m_parent).append(" -> ").append(count).append(" [").append(getEdgeStyle(op)).append("];\n");
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
	
	protected static String getVertexStyle(Operator<?> op)
	{
		Value v = op.getValue();
		String shape = "";
		if (op instanceof EventLeaf)
		{
			shape = "shape=\"oval\",";
		}
		if (v == Value.TRUE)
		{
			if (op.isDeleted())
			{
				return shape + "style=\"dashed,filled\",color=\"grey\",fillcolor=\"darkseagreen1\"";
			}
			return shape + "style=\"filled\",fillcolor=\"chartreuse\"";
		}
		if (v == Value.FALSE)
		{
			if (op.isDeleted())
			{
				return shape + "style=\"dashed,filled\",color=\"grey\",fillcolor=\"lightpink\"";
			}
			return shape + "style=\"filled\",fillcolor=\"firebrick1\"";
		}
		if (op.isDeleted())
		{
			return shape + "style=\"dashed,filled\",color=\"grey\",fillcolor=\"ivory2\"";
		}
		return shape + "style=\"filled\",fillcolor=\"white\"";
	}
		
	protected static String getEdgeStyle(Operator<?> op)
	{
		if (op.isDeleted())
		{
			return "style=\"dashed\",color=\"grey\"";
		}
		return "style=\"solid\"";
	}	

}
