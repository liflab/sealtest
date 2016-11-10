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

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;

/**
 * Cayley-graph experiment based on a finite-state automaton
 * 
 * @author Sylvain Hallé
 */
public abstract class CayleyAutomatonExperiment<U> extends CayleyCategoryCoverageExperiment<AtomicEvent,U>
{
	public static String PROPERTY_NAME = "property-name";
	
	/**
	 * The automaton
	 */
	protected transient Automaton m_automaton;

	public CayleyAutomatonExperiment() 
	{
		super();
		describe(PROPERTY_NAME, "The name of the property represented by the automaton");
	}
	
	public CayleyAutomatonExperiment(Automaton a) 
	{
		super();
		m_automaton = a;
	}
	
	public void setAutomaton(Automaton a)
	{
		m_automaton = a;
	}
	
	public void setPropertyName(String name)
	{
		setInput(PROPERTY_NAME, name);
	}
	
	/**
	 * Fills the automaton and title for this experiments from the contents
	 * read from a scanner
	 * @param exp The experiment to fill
	 * @param scanner The scanner
	 */
	public static void fillExperiment(CayleyAutomatonExperiment<?> exp, Scanner scanner)
	{
		Pattern pat_title = Pattern.compile("Title: (.*)");
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if (line.startsWith("digraph"))
				break;
			Matcher mat = pat_title.matcher(line);
			if (mat.find())
			{
				exp.setPropertyName(mat.group(1));
			}
		}
		exp.setAutomaton(Automaton.parseDot(scanner));
	}
}
