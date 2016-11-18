package ca.uqac.lif.ecp.ltl;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;

public class AtomicLtlCayleyGraphFactoryTest
{
	protected static final HologramFormatter<AtomicEvent> formatter = new HologramFormatter<AtomicEvent>();
	
	/*@Test
	public void test1() throws BuildException
	{
		String expression = "a";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new IdentityHologramTransformation<AtomicEvent>();
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		graph.setLabelFormatter(formatter);
		System.out.println(graph);
	}*/
	
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
		graph.setLabelFormatter(formatter);
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
		assertEquals(3, graph.getVertexCount());
		//graph.setLabelFormatter(formatter);
		//System.out.println(graph);
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
		assertEquals(15, graph.getVertexCount());
		//graph.setLabelFormatter(formatter);
		//System.out.println(graph);
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
		//assertEquals(15, graph.getVertexCount());
		graph.setLabelFormatter(formatter); System.out.println(graph);
	}
	
	@Test
	public void test5c() throws BuildException
	{
		//String expression = "G (a -> (X b))";
		String expression = "G (a | (! b))";
		AtomicParserBuilder builder = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = builder.build();
		HologramTransformation<AtomicEvent> ht = new HologramComposition<AtomicEvent>(new FailFastDeletion<AtomicEvent>(), new RootChildDeletion<AtomicEvent>(2));
		HologramFunction<AtomicEvent> hf = new HologramFunction<AtomicEvent>(op, ht);
		AtomicLtlCayleyGraphFactory factory = new AtomicLtlCayleyGraphFactory();
		CayleyGraph<AtomicEvent,Operator<AtomicEvent>> graph = factory.getGraph(hf);
		assertNotNull(graph);
		//assertEquals(15, graph.getVertexCount());
		graph.setLabelFormatter(formatter); System.out.println(graph);
	}
}
