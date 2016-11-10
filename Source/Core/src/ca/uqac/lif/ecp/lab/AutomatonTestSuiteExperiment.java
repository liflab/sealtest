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

import ca.uqac.lif.ecp.TestSuite;
import ca.uqac.lif.ecp.TraceGenerator;
import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.atomic.Automaton;

/**
 * Test suite experiment based on a specification expressed as a
 * finite-state automaton
 * 
 * @author Sylvain Hallé
 */
public abstract class AutomatonTestSuiteExperiment extends TestSuiteGenerationExperiment
{
	/**
	 * The automaton this experiment is based from
	 */
	protected transient Automaton m_automaton;
	
	AutomatonTestSuiteExperiment()
	{
		super();
	}
	
	/**
	 * Creates a new instance of this experiment
	 * @param a The automaton this experiment is based on
	 */
	AutomatonTestSuiteExperiment(Automaton a)
	{
		super();
		m_automaton = a;
	}
	
	@Override
	public final TestSuite<?> getTestSuite() 
	{
		TraceGenerator<AtomicEvent> generator = getGenerator();
		TestSuite<AtomicEvent> suite = generator.generateTraces();
		return suite;
	}
	
	/**
	 * Returns an instance of the trace generator that will generate the
	 * test suite for this experiment
	 * @return The trace generator
	 */
	public abstract TraceGenerator<AtomicEvent> getGenerator();

}
