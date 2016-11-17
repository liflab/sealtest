package ca.uqac.lif.ecp.ltl;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;

public class AtomicLtlCayleyGraphFactoryTest
{
	protected static final HologramFormatter<AtomicEvent> formatter = new HologramFormatter<AtomicEvent>();
	
	@Test
	public void test1() throws BuildException
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
}
