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
import ca.uqac.lif.ecp.Trace;

/**
 * Experiment that computes the test suite "live", i.e. its results are not
 * pre-computed.
 * 
 * @author Sylvain Hallé
 */
public class LiveGenerationExperiment extends TestSuiteGenerationExperiment 
{	
	/**
	 * The number of times each experiment is repeated
	 */
	protected static final transient int s_numRepetitions = 3;
	
	public LiveGenerationExperiment(TestSuiteProvider<?> provider)
	{
		super();
		m_provider = provider;
		m_provider.write(this);
	}
	
	@Override
	public Status execute() 
	{
		int total_size = 0;
		int total_length = 0;
		int total_shortest = 0;
		int total_longest = 0;
		for (int rep_count = 0; rep_count < s_numRepetitions; rep_count++)
		{
			long time_start = System.currentTimeMillis();
			TestSuite<?> suite = m_provider.getTestSuite();
			long time_end = System.currentTimeMillis();
			if (suite == null)
			{
				setErrorMessage("Could not generate a test suite");
				return Status.FAILED;
			}
			total_size += suite.size();
			total_length += suite.getTotalLength();
			int len_max = -1;
			int len_min = -1;
			for (Trace<?> t : suite)
			{
				int len = t.size();
				if (len_max == -1 || len > len_max)
				{
					len_max = len;
				}
				if (len_min == -1 || len < len_min)
				{
					len_min = len;
				}			
			}
			total_shortest += len_min;
			total_longest += len_max;
			write(DURATION, (time_end - time_start) / 1000f);
		}
		write(SIZE, (float) total_size / (float) s_numRepetitions);
		write(TOTAL_LENGTH, (float) total_length / (float) s_numRepetitions);
		write(SHORTEST, (float) total_shortest / (float) s_numRepetitions);
		write(LONGEST, (float) total_longest / (float) s_numRepetitions);
		return Status.DONE;
	}
}
