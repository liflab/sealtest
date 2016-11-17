package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class GraphvizHologramRenderer<T extends Event> extends HologramVisitor<T> 
{
	/**
	 * Static variable to check if we are running in Windows.
	 * In such a case, Graphviz cannot display correctly Unicode characters,
	 * and we must fall-back to a more primitive ASCII representation. 
	 */
	protected static final transient boolean s_isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	
	protected int m_parent = -1;
	
	protected StringBuilder m_buffer = new StringBuilder();

	@Override
	public void visit(Operator<T> op, int count) 
	{
		if (m_parent == -1)
		{
			m_buffer.append("digraph {\n nodesep=0.125;\n ranksep=0.25;\n node [shape=\"rectangle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];\n edge [arrowhead=\"none\"];\n");
		}
		m_buffer.append(" ").append(count).append(" [label=<").append(beautifySymbol(op)).append(">,").append(getVertexStyle(op)).append("];\n");
		if (m_parent != -1)
		{
			m_buffer.append(" ").append(m_parent).append(" -> ").append(count).append(" [").append(getEdgeStyle(op)).append("];\n");
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
			if (op.isDeleted())
			{
				return "fillcolor=\"white\",shape=\"none\",fontcolor=\"grey\"";
			}
			return "fillcolor=\"white\",shape=\"none\"";
			
		}
		if (v == Value.TRUE)
		{
			if (op.isDeleted())
			{
				return shape + "style=\"dashed,filled\",color=\"grey\",fontcolor=\"grey\",fillcolor=\"darkseagreen1\"";
			}
			return shape + "style=\"filled\",fillcolor=\"chartreuse\"";
		}
		if (v == Value.FALSE)
		{
			if (op.isDeleted())
			{
				return shape + "style=\"dashed,filled\",color=\"grey\",fontcolor=\"grey\",fillcolor=\"lightpink\"";
			}
			return shape + "style=\"filled\",fillcolor=\"firebrick1\"";
		}
		if (op.isDeleted())
		{
			return shape + "style=\"dashed,filled\",color=\"grey\",fontcolor=\"grey\",fillcolor=\"ivory2\"";
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
	
	/**
	 * Prints a "beautified" symbol for an operator, for
	 * the purpose of displaying in the tree 
	 * @param op The operator
	 */
	protected static String beautifySymbol(Operator<?> op)
	{
		if (op instanceof And)
		{
			if (s_isWindows)
			{
				return "&&";
			}
			return "&and;";
		}
		if (op instanceof Or)
		{
			if (s_isWindows)
			{
				return "||";
			}
			return "&or;";
		}
		if (op instanceof Globally)
		{
			return "<B>G</B>";
		}
		if (op instanceof Eventually)
		{
			return "<B>F</B>";
		}
		if (op instanceof Next)
		{
			return "<B>X</B>";
		}
		return op.getRootSymbol();
	}

}
