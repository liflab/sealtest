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

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;

public class AtomicLtlCayleyGraphFactoryTest
{
	protected static final HologramFormatter<AtomicEvent> s_formatter = new HologramFormatter<AtomicEvent>();
	
	protected static final HtmlBeautifier<AtomicEvent> s_beautifier = new HtmlBeautifier<AtomicEvent>();
	
	/**
	 * The folder where the graph and its holograms will be written
	 * for viewing by an external program
	 */
	protected static final String s_graphFolder = "./";
	
	@Test
	public void test1() throws BuildException
	{
		String expression = "F a";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new RootChildDeletion<AtomicEvent>(1);
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		getGraphDot(graph, "<TABLE BORDER=\"0\"><TR><TD>" + s_beautifier.beautify(op) + "</TD></TR><TR><TD>" + ht.toString() + "</TD></TR></TABLE>", s_graphFolder);
	}
	
	@Test
	public void test2() throws BuildException
	{
		String expression = "a | b";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new IdentityHologramTransformation<AtomicEvent>();
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		graph.setLabelFormatter(s_formatter);
		System.out.println(graph);
	}
	
	@Test
	public void test3() throws BuildException
	{
		String expression = "G a";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new RootChildDeletion<AtomicEvent>(2);
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		assertEquals(3, graph.getVertexCount());
		//graph.setLabelFormatter(formatter);
		//System.out.println(graph);
	}
	
	@Test
	public void test4a() throws BuildException
	{
		String expression = "G (a | b)";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new RootChildDeletion<AtomicEvent>(2);
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		assertEquals(7, graph.getVertexCount());
		//graph.setLabelFormatter(formatter);
		//System.out.println(graph);
	}
	
	@Test
	public void test4b() throws BuildException
	{
		String expression = "G (a | b)";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new HologramComposition<AtomicEvent>(new LeafDeletion<AtomicEvent>(), new RootChildDeletion<AtomicEvent>(2));
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		// graph.setLabelFormatter(formatter); System.out.println(graph);
		assertEquals(7, graph.getVertexCount());
	}
	
	@Test
	public void test5a() throws BuildException
	{
		String expression = "G (a -> (X b))";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new RootChildDeletion<AtomicEvent>(2);
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		HtmlBeautifier<AtomicEvent> hb = new HtmlBeautifier<AtomicEvent>();
		op.acceptPostfix(hb, true);
		getGraphDot(graph, "<TABLE BORDER=\"0\"><TR><TD>" + s_beautifier.beautify(op) + "</TD></TR><TR><TD>" + ht.toString() + "</TD></TR></TABLE>", s_graphFolder);
		assertEquals(17, graph.getVertexCount());
	}
	
	@Test
	public void test5b() throws BuildException
	{
		String expression = "G (a -> (X b))";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new HologramComposition<AtomicEvent>(new LeafDeletion<AtomicEvent>(), new RootChildDeletion<AtomicEvent>(2));
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		getGraphDot(graph, "<TABLE BORDER=\"0\"><TR><TD>" + s_beautifier.beautify(op) + "</TD></TR><TR><TD>" + ht.toString() + "</TD></TR></TABLE>", s_graphFolder);
		assertEquals(17, graph.getVertexCount());
	}
	
	@Test
	public void test6() throws BuildException
	{
		String expression = "G (a & (! b))";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new HologramComposition<AtomicEvent>(new FailFastDeletion<AtomicEvent>(), new RootChildDeletion<AtomicEvent>(3));
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		//getGraphDot(graph, "<TABLE BORDER=\"0\"><TR><TD>" + s_beautifier.beautify(op) + "</TD></TR><TR><TD>" + ht.toString() + "</TD></TR></TABLE>", s_graphFolder);
		assertEquals(4, graph.getVertexCount());
	}
	
	@Test
	public void test7() throws BuildException
	{
		String expression = "G (a -> (X (b | c)))";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new HologramComposition<AtomicEvent>(new RootChildDeletion<AtomicEvent>(4), new LeafDeletion<AtomicEvent>(), new PolarityDeletion<AtomicEvent>());
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		getGraphDot(graph, "<TABLE BORDER=\"0\"><TR><TD>" + s_beautifier.beautify(op) + "</TD></TR><TR><TD>" + ht.toString() + "</TD></TR></TABLE>", s_graphFolder);
		assertEquals(17, graph.getVertexCount());
	}
	
	protected void getGraphDot(CayleyGraph<AtomicEvent,Operator<AtomicEvent>> g, String title, String path)
	{
		LtlCayleyGraphRenderer<AtomicEvent> renderer = new LtlCayleyGraphRenderer<AtomicEvent>(g);
		renderer.setTitle(title);
		if (path != null)
		{
			renderer.generateHolograms(path);
		}
		renderer.writeDot(path + "graph.dot");
	}
}
