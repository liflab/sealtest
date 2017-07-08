/*
    Log trace triaging and etc.
    Copyright (C) 2016-2017 Sylvain Hallé

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
package examples.automaton;

import java.io.IOException;

import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.AtomicGraphPlotter;
import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.ecp.atomic.ElseEvent;
import ca.uqac.lif.ecp.graphs.Vertex;

/**
 * Shows a few functions for creating and manipulating finite-state automata.
 * @author Sylvain Hallé
 */
public class AutomatonExample
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException 
	{
		// Create a simple automaton
		AtomicEvent a = new AtomicEvent("a");
		AtomicEvent b = new AtomicEvent("b");
		AtomicEvent c = new AtomicEvent("c");
		Vertex<AtomicEvent> v0 = new Vertex<AtomicEvent>(0);
		Vertex<AtomicEvent> v1 = new Vertex<AtomicEvent>(1);
		Vertex<AtomicEvent> v2 = new Vertex<AtomicEvent>(2);
		Vertex<AtomicEvent> v3 = new Vertex<AtomicEvent>(3);
		Automaton aut = new Automaton();
		aut.add(v0, v1, v2, v3);
		v0.add(new Edge<AtomicEvent>(0, a, 0));
		v0.add(new Edge<AtomicEvent>(0, b, 1));
		v0.add(new Edge<AtomicEvent>(0, c, 1));
		v1.add(new Edge<AtomicEvent>(1, a, 1));
		v1.add(new Edge<AtomicEvent>(1, b, 0));
		v1.add(new Edge<AtomicEvent>(1, c, 2));
		v2.add(new Edge<AtomicEvent>(2, a, 3));
		v2.add(new Edge<AtomicEvent>(2, ElseEvent.instance, 0));
		v3.add(new Edge<AtomicEvent>(3, ElseEvent.instance, 0));
		aut.setInitialVertexId(0);

		// Render this automaton as a Graphviz file
		AtomicGraphPlotter<String> plotter = aut.plotter();
		System.out.println(plotter.toDot());
		
		// Print the adjacency matrix
		float[][] matrix = aut.getAdjacencyMatrix();
		System.out.println(formatMatrix(matrix));
	}
	
	protected static String formatMatrix(float[][] matrix)
	{
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				out.append(matrix[i][j]).append(" ");
			}
			out.append("\n");
		}
		return out.toString();
	}

}
