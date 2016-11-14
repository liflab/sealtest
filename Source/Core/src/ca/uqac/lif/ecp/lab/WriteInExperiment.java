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

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Experiment whose data is not computed, but rather fetched from
 * an external source. This is useful to integrate in a lab results
 * from other experiments (such as results from someone else's paper
 * on the same input data).
 * 
 * @author Sylvain Hallé
 *
 */
public class WriteInExperiment extends TestSuiteGenerationExperiment 
{
	public static final transient String FROM = "from";
	public static final transient String BIBSOURCE = "bibsource";
	
	public WriteInExperiment(Scanner scanner)
	{
		describe(FROM, "Research paper from which this result was taken");
		describe(BIBSOURCE, "BibTeX reference of the research paper");
		describe(CombinatorialTriagingFunctionProvider.FUNCTION, CombinatorialTriagingFunctionProvider.FUNCTION_DESCRIPTION);
		describe(CombinatorialTriagingFunctionProvider.STRENGTH, CombinatorialTriagingFunctionProvider.STRENGTH_DESCRIPTION);
		describe(AutomatonProvider.PROPERTY_NAME, AutomatonProvider.PROPERTY_DESCRIPTION);
		Pattern from_pat = Pattern.compile("From:(.*)");
		Pattern bib_pat = Pattern.compile("BibSource:(.*)");
		Matcher mat;
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
			{
				continue;
			}
			mat = from_pat.matcher(line);
			if (mat.find())
			{
				String s = mat.group(1).trim();
				setInput("from", s);
				continue;
			}
			mat = bib_pat.matcher(line);
			if (mat.find())
			{
				String s = mat.group(1).trim();
				setInput("bibsource", s);
				continue;
			}
			String[] parts = line.split("\\t+");
			setInput(AutomatonProvider.PROPERTY_NAME, parts[0].trim());
			setInput(CombinatorialTriagingFunctionProvider.FUNCTION, parts[1].trim());
			setInput(CombinatorialTriagingFunctionProvider.STRENGTH, Integer.parseInt(parts[2].trim()));
			setInput(SIZE, Integer.parseInt(parts[3].trim()));
			setInput(TOTAL_LENGTH, Integer.parseInt(parts[4].trim()));
			setInput(DURATION, Float.parseFloat(parts[5].trim()));
		}
	}


	@Override
	public final Status execute() 
	{
		// Nothing to do
		return Status.DONE;
	}

}
