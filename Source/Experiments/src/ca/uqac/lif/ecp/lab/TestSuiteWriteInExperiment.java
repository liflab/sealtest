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

import ca.uqac.lif.ecp.lab.fsm.AutomatonProvider;
import ca.uqac.lif.ecp.lab.fsm.CombinatorialTriagingFunctionProvider;
import ca.uqac.lif.parkbench.CloneableExperiment;

/**
 * Experiment whose data is not computed, but rather fetched from
 * an external source. This is useful to integrate in a lab results
 * from other experiments (such as results from someone else's paper
 * on the same input data).
 * 
 * @author Sylvain Hallé
 *
 */
public class TestSuiteWriteInExperiment extends TestSuiteGenerationExperiment implements CloneableExperiment<TestSuiteWriteInExperiment>
{
	public static final transient String FROM = "From";
	public static final transient String BIBSOURCE = "BibSource";
	
	public TestSuiteWriteInExperiment()
	{
		super();
		describe(FROM, "Research paper from which this result was taken");
		describe(BIBSOURCE, "BibTeX reference of the research paper");
		describe(CombinatorialTriagingFunctionProvider.FUNCTION, CombinatorialTriagingFunctionProvider.FUNCTION_DESCRIPTION);
		describe(CombinatorialTriagingFunctionProvider.STRENGTH, CombinatorialTriagingFunctionProvider.STRENGTH_DESCRIPTION);
		describe(AutomatonProvider.PROPERTY_NAME, AutomatonProvider.PROPERTY_DESCRIPTION);
		addKeyToHide(FROM);
		addKeyToHide(BIBSOURCE);
	}
	
	@Override
	public final Status execute() 
	{
		// Nothing to do
		return Status.DONE;
	}

	@Override
	public final TestSuiteWriteInExperiment newExperiment() 
	{
		return new TestSuiteWriteInExperiment();
	}

}
