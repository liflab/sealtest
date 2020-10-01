/*
    Log trace triaging and etc.
    Copyright (C) 2016-2020 Sylvain Hallé

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
package ca.uqac.lif.ecp.atomic;

import java.io.PrintStream;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.graphs.Vertex;

/**
 * Renders an automaton in DOT format.
 */
public class DotAutomatonRenderer implements AutomatonRenderer
{
	/**
	 * Creates a new instance of the renderer
	 */
	public DotAutomatonRenderer()
	{
		super();
	}
	
	@Override
	public void render(Automaton aut, PrintStream ps)
	{
		ps.println("digraph G {");
		for (Vertex<AtomicEvent> v : aut.getVertices())
		{
			ps.println(v.getId() + "[label=\"" + v.getId() + "\"];");
			for (Edge<AtomicEvent> e : v.getEdges())
			{
				ps.println(e.getSource() + " -> " + e.getDestination() + " [label=\"" + e.getLabel() + "\"];");
			}
		}
		ps.println("}");
	}
	
	@Override
	public void reset()
	{
		// Nothing to do
	}
}