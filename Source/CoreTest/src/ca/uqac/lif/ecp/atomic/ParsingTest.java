package ca.uqac.lif.ecp.atomic;

import static ca.uqac.lif.ecp.atomic.TestSettings.loadAutomaton;
import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.ecp.CayleyGraph.IsomorphismException;

public class ParsingTest 
{
	@Test
	public void testStateHistory1d1() throws IsomorphismException
	{
		Automaton aut = loadAutomaton("test3.dot");
		assertNotNull(aut);
		assertEquals(3, aut.getVertexCount());
		assertEquals(9, aut.getEdgeCount());
	}
}
