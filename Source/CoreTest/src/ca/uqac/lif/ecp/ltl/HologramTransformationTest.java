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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;

/**
 * Unit tests for hologram transformations. These tests will not fail,
 * but will rather generate pictures that you can manually look at to
 * make sure that the transformations are done correctly.
 * <p>
 * Some day, we might want to automate that verification step. Wanna help?
 * 
 * @author Sylvain Hallé
 *
 */
@RunWith(Parameterized.class)
public class HologramTransformationTest
{
	static final HologramTransformationTestCase[] s_queries = readQueries();
	
	int m_queryNumber = 0;
	
	public HologramTransformationTest(int query_number)
	{
		super();
		m_queryNumber = query_number;
	}
	
	@Test
	public void runTest() throws BuildException
	{
		HologramTransformationTestCase t_case = s_queries[m_queryNumber];
		AtomicParserBuilder builder = new AtomicParserBuilder(t_case.m_query);
		Operator<AtomicEvent> formula = builder.build();
		assertNotNull(formula);
		HtmlBeautifier<AtomicEvent> b = new HtmlBeautifier<AtomicEvent>();
		formula.acceptPostfix(b);
		String[] events = t_case.m_trace.split(",");
		for (String s : events)
		{
			AtomicEvent e = new AtomicEvent(s);
			formula.evaluate(e);
		}
		HologramTransformation<AtomicEvent> trans = t_case.m_transformation;
		Operator<AtomicEvent> tree = trans.transform(formula);
		GraphvizHologramRenderer<AtomicEvent> renderer = new GraphvizHologramRenderer<AtomicEvent>();
		renderer.setCaption("<TABLE BORDER=\"0\"><TR><TD>" + b.getString() + "</TD></TR><TR><TD>" + t_case.m_trace + "</TD></TR><TR><TD>" + t_case.m_transformation + "</TD></TR></TABLE>");
		tree.acceptPrefix(renderer, true);
		String filename = m_queryNumber + ".dot";
		writeToFile(filename, renderer.toDot());
	}
	
	/**
	 * Parses a text file and creates a number of hologram unit test cases
	 * from its content
	 * @return An array of test cases
	 */
	static HologramTransformationTestCase[] readQueries()
	{
		Vector<HologramTransformationTestCase> queries = new Vector<HologramTransformationTestCase>();
		Scanner scanner = new Scanner(HologramTransformationTest.class.getResourceAsStream("all-expressions.ltl"));
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			String[] parts = line.split("\\t+");
			HologramTransformationTestCase t_case = new HologramTransformationTestCase();
			t_case.m_query = parts[0].trim();
			t_case.m_trace = parts[1].trim();
			String transformation = parts[2].trim();
			if (transformation.compareTo("FFD") == 0)
			{
				t_case.m_transformation = new FailFastDeletion<AtomicEvent>();
			}
			else if (transformation.compareTo("LD") == 0)
			{
				t_case.m_transformation = new LeafDeletion<AtomicEvent>();
			}
			else if (transformation.compareTo("PD") == 0)
			{
				t_case.m_transformation = new PolarityDeletion<AtomicEvent>();
			}
			else if (transformation.startsWith("DF"))
			{
				int depth = Integer.parseInt(transformation.substring(2).trim());
				t_case.m_transformation = new DepthFiltering<AtomicEvent>(depth);
			}
			else
			{
				t_case.m_transformation = new IdentityHologramTransformation<AtomicEvent>();
			}
			queries.add(t_case);
		}
		scanner.close();
		HologramTransformationTestCase[] out = new HologramTransformationTestCase[queries.size()];
		queries.toArray(out);
		return out;
	}
	
	@Parameters(name = "{index}: query {0}")
	public static Collection<Integer[]> getQueries()
	{
		List<Integer[]> ints = new ArrayList<Integer[]>(s_queries.length);
		for (int i = 0; i < s_queries.length; i++)
		{
			Integer[] a_i = new Integer[1];
			a_i[0] = i;
			ints.add(a_i);
		}
		return ints;
	}
	
	/**
	 * Class representing a specific test case for hologram transformations.
	 * A test case consists of an LTL formula, a trace, and a hologram
	 * transformation to be applied to the resulting evaluation tree
	 */
	static class HologramTransformationTestCase
	{
		String m_query;
		String m_trace;
		HologramTransformation<AtomicEvent> m_transformation;
	}
	
	/**
	 * Writes a string into a file
	 * @param filename
	 * @param contents
	 */
	public static void writeToFile(String filename, String contents)
	{
		try 
		{
			FileOutputStream fos = new FileOutputStream(new File(filename));
			fos.write(contents.getBytes());
			fos.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
