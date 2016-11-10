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
import ca.uqac.lif.parkbench.Experiment;

/**
 * Experiment that consists in generating a test suite
 * 
 * @author Sylvain Hallé
 */
public abstract class TestSuiteGenerationExperiment extends Experiment
{
	public static final String SIZE = "size";
	public static final String TOTAL_LENGTH = "total-length";
	public static final String DURATION = "duration";
	
	public TestSuiteGenerationExperiment()
	{
		setDescription("Experiment that consists in generating a test suite");
		describe(SIZE, "Number of sequences in the test suite");
		describe(TOTAL_LENGTH, "Total length of all sequences in the test suite");
		describe(DURATION, "Time (in seconds) to generate the test suite");
	}
	
	@Override
	public Status execute() 
	{
		long time_start = System.currentTimeMillis();
		TestSuite<?> suite = getTestSuite();
		long time_end = System.currentTimeMillis();
		if (suite == null)
		{
			setErrorMessage("Could not generate a test suite");
			return Status.FAILED;
		}
		write(SIZE, suite.size());
		write(TOTAL_LENGTH, suite.getTotalLength());
		write(DURATION, (time_end - time_start) / 1000f);
		return Status.DONE;
	}
	
	/**
	 * Generates a test suite. The actual method for generating the suite
	 * depends on the concrete (i.e. non-abstract) this experiment belongs to
	 * @return A test suite
	 */
	public abstract TestSuite<?> getTestSuite();
}
