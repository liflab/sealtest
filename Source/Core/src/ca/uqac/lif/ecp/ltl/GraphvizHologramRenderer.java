/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class GraphvizHologramRenderer<T extends Event> extends HologramVisitor<T> 
{	
	/**
	 * The parent node of the current node
	 */
	protected int m_parent = -1;
	
	/**
	 * The buffer where the graph will be written
	 */
	protected StringBuilder m_buffer = new StringBuilder();
	
	/**
	 * The caption to write to the graph
	 */
	protected String m_caption = "";
	
	/**
	 * Sets a caption to the graph
	 * @param caption The caption
	 */
	public void setCaption(String caption)
	{
		m_caption = caption;
	}
	
	@Override
	public void visit(Operator<T> op, int count) 
	{
		if (m_parent == -1)
		{
			m_buffer.append("digraph {\n nodesep=0.125;\n ranksep=0.25;\n node [shape=\"rectangle\",height=0.3,width=0.3,fixedsize=\"true\",style=\"filled\"];\n edge [arrowhead=\"none\"];\n");
			if (!m_caption.isEmpty())
			{
				m_buffer.append(" label=<").append(m_caption).append(">;\n");
			}
		}
		m_buffer.append(" ").append(count).append(" [label=<").append(HtmlBeautifier.beautifySymbol(op)).append(">,").append(getVertexStyle(op)).append("];\n");
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
}
