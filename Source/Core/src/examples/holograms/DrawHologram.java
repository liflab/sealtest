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
package examples.holograms;

import ca.uqac.lif.ecp.Trace;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.AtomicTrace;
import ca.uqac.lif.ecp.ltl.AtomicParserBuilder;
import ca.uqac.lif.ecp.ltl.DepthFiltering;
import ca.uqac.lif.ecp.ltl.FailFastDeletion;
import ca.uqac.lif.ecp.ltl.GraphvizHologramRenderer;
import ca.uqac.lif.ecp.ltl.HologramTransformation;
import ca.uqac.lif.ecp.ltl.IdentityHologramTransformation;
import ca.uqac.lif.ecp.ltl.LeafDeletion;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;
import ca.uqac.lif.ecp.ltl.PolarityDeletion;

/**
 * Draw a hologram from a trace and an LTL formula
 * @author Sylvain Hallé
 */
public class DrawHologram
{
	public static void main(String[] args) throws BuildException
	{
		// Define the formula and parse
		String formula_string = "G ((a) -> (X (b | c)))";
		AtomicParserBuilder parser = new AtomicParserBuilder(formula_string);
		Operator<AtomicEvent> op = parser.build();
		// Define the trace, parse and evaluate
		String trace_string = "c,b,a,d";
		Trace<AtomicEvent> trace = AtomicTrace.readTrace(trace_string);
		for (AtomicEvent ae : trace)
			op.evaluate(ae);
		// Define hologram transformation and transform
		//HologramTransformation<AtomicEvent> df = //new IdentityHologramTransformation<AtomicEvent>();//new DepthFiltering<AtomicEvent>(2);
		HologramTransformation<AtomicEvent> df = new PolarityDeletion<AtomicEvent>();
		Operator<AtomicEvent> new_op = df.transform(op);
		// Draw resulting hologram
		GraphvizHologramRenderer<AtomicEvent> renderer = new GraphvizHologramRenderer<AtomicEvent>();
		new_op.acceptPrefix(renderer, true);
		System.out.println(renderer.toDot());
	}

}
