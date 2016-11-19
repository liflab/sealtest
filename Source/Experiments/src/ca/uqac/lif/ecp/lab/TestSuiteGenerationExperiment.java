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

import ca.uqac.lif.parkbench.Experiment;

/**
 * Experiment that consists in generating a test suite
 * 
 * @author Sylvain Hallé
 */
public abstract class TestSuiteGenerationExperiment extends Experiment
{
	public static final transient String SIZE = "Size";
	public static final transient String TOTAL_LENGTH = "Length";
	public static final transient String LONGEST = "Longest";
	public static final transient String SHORTEST = "Shortest";
	public static final transient String DURATION = "Time";
	public static final transient String METHOD = "Method";
	
	/**
	 * The provider used to generate the test suite
	 */
	protected transient TestSuiteProvider<?> m_provider;
		
	public TestSuiteGenerationExperiment()
	{
		this(null);
	}
	
	public TestSuiteGenerationExperiment(TestSuiteProvider<?> provider)
	{
		super();
		m_provider = provider;
		setDescription("Experiment that consists in generating a test suite");
		describe(SIZE, "Number of sequences in the test suite");
		describe(TOTAL_LENGTH, "Total length of all sequences in the test suite");
		describe(LONGEST, "Length of the longest sequence");
		describe(SHORTEST, "Length of the shortest sequence");
		describe(DURATION, "Time (in seconds) to generate the test suite");
		describe(METHOD, "Method used to generate the test sequence");
	}
	
}
