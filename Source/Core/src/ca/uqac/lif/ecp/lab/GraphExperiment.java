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
package ca.uqac.lif.ecp.lab;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.atomic.Automaton;
import ca.uqac.lif.parkbench.Experiment;

public abstract class GraphExperiment extends Experiment
{
	protected transient Automaton m_graph;
	
	GraphExperiment()
	{
		super();
		describe("property", "Name of the property this automaton represents");
	}
	
	public GraphExperiment(Scanner scanner)
	{
		this();
		Pattern pat_title = Pattern.compile("Title: (.*)");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.startsWith("digraph"))
				break;
			Matcher mat = pat_title.matcher(line);
			if (mat.find())
			{
				setInput("property", mat.group(1));
			}
		}
		Automaton g = Automaton.parseDot(scanner);
		m_graph = g;
	}
	
	public GraphExperiment setGraph(Automaton g)
	{
		m_graph = g;
		return this;
	}
	
	public Automaton getGraph()
	{
		return m_graph;
	}
}
