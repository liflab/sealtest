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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.CayleyVertexLabelling;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.graphs.Vertex;
import ca.uqac.lif.ecp.ltl.Operator.Value;
import ca.uqac.lif.structures.MathSet;

public class LtlCayleyGraphRenderer<T extends Event> 
{
	/**
	 * The graph to render
	 */
	protected CayleyGraph<T,Operator<T>> m_graph;
	
	/**
	 * Whether to generate auxiliary DOT files containing the holograms
	 */
	protected boolean m_generateHolograms = false;
	
	/**
	 * The path where to write the DOT files for each hologram
	 */
	protected String m_path = "./";
	
	public LtlCayleyGraphRenderer(CayleyGraph<T,Operator<T>> graph)
	{
		super();
		m_graph = graph;
	}
	
	/**
	 * Tells the renderer to generate the holograms for each vertex
	 * and to write them as DOT files into the given path
	 * @param path The path
	 */
	public void generateHolograms(String path)
	{
		m_generateHolograms = true;
		m_path = path;
	}
	
	/**
	 * Writes a DOT file corresponding to the Cayley graph
	 * @param filename The file to write to
	 * @return A DOT string
	 */
	public void writeDot(String filename)
	{
		String s = toDot();
		writeToFile(s, filename);
	}
	
	/**
	 * Gets a DOT file corresponding to the Cayley graph
	 * @return A DOT string
	 */
	public String toDot()
	{
		StringBuilder out = new StringBuilder();
		StringBuilder edge_buffer = new StringBuilder();
		HologramHasher<T> hasher = new HologramHasher<T>();
		out.append("digraph {\n node[shape=\"rectangle\",style=\"filled\"];\n");
		CayleyVertexLabelling<Operator<T>> labelling = m_graph.getLabelling();
		for (Vertex<T> v : m_graph.getVertices())
		{
			int id = v.getId();
			MathSet<Operator<T>> label = labelling.get(id);
			Operator<T> op = null;
			for (Operator<T> o : label)
			{
				op = o;
				break;
			}
			if (m_generateHolograms)
			{
				String holo_dot_filename = m_path + id + ".dot";
				String holo_svg_filename = m_path + id + ".svg";
				out.append(" ").append(id).append(" [label=<<TABLE BORDER=\"0\"><TR><TD HREF=\"").append(holo_svg_filename).append("\">").append(hasher.getHash(op)).append("</TD></TR></TABLE>>");
				renderHologram(op, holo_dot_filename);
			}
			else
			{
				out.append(" ").append(id).append(" [label=<").append(hasher.getHash(op)).append(">");
			}
			if (op.getValue() == Value.FALSE)
			{
				out.append(",fillcolor=\"firebrick1\"");
			}
			else if (op.getValue() == Value.TRUE)
			{
				out.append(",fillcolor=\"chartreuse\"");
			}
			out.append("];\n");
			for (Edge<T> e : v.getEdges())
			{
				edge_buffer.append(" ").append(e.getSource()).append(" -> ").append(e.getDestination()).append(" [label=<").append(e.getLabel()).append(">];\n");
			}
		}
		out.append(edge_buffer);
		out.append("}");
		return out.toString();
	}
	
	/**
	 * Renders a hologram and prints it to a DOT file
	 * @param op The hologram
	 * @param filename The file to write to
	 */
	protected void renderHologram(Operator<T> op, String filename)
	{
		GraphvizHologramRenderer<T> renderer = new GraphvizHologramRenderer<T>();
		op.acceptPrefix(renderer, true);
		String contents = renderer.toDot();
		writeToFile(contents, filename);
	}
	
	protected void writeToFile(String contents, String filename)
	{
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			fos.write(contents.getBytes());
			fos.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
