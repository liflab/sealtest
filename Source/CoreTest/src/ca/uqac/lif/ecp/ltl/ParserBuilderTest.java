package ca.uqac.lif.ecp.ltl;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;

public class ParserBuilderTest
{
	@Test
	public void test1() throws BuildException
	{
		String expression = "p";
		AtomicParserBuilder apb = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = apb.build();
		assertNotNull(op);
		assertTrue(op instanceof Atom);
	}
	
	@Test
	public void test2() throws BuildException
	{
		String expression = "p & q";
		AtomicParserBuilder apb = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = apb.build();
		assertNotNull(op);
		assertTrue(op instanceof And);
	}
	
	@Test
	public void test3() throws BuildException
	{
		String expression = "G (p & (!q))";
		AtomicParserBuilder apb = new AtomicParserBuilder(expression);
		Operator<AtomicEvent> op = apb.build();
		assertNotNull(op);
		assertTrue(op instanceof Globally);
		assertEquals(5, op.size());
	}
}
