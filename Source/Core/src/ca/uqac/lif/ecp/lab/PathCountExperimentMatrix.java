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

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.structures.Matrix;

public class PathCountExperimentMatrix extends PathCountExperiment 
{
	public PathCountExperimentMatrix(Scanner scanner, int max_length) 
	{
		super(scanner, max_length);
	}

	@Override
	public Status execute() 
	{
		if (m_graph == null)
		{
			setErrorMessage("Could not find resource");
			return Status.FAILED;
		}
		float[][] M = m_graph.getAdjacencyMatrix();
		List<Integer> labels = m_graph.getVertexLabels();
		Collections.sort(labels);
		// Create empty vector, put 1 as its first component
		float[] V = new float[M[0].length];
		for (int i = 0; i < V.length; i++)
		{
			V[i] = 0;
		}
		V[0] = 1;
		// Repeatedly multiply M by V 
		float[] V_prime = null;
		for (int it_count = 0; it_count < readInt("max-length"); it_count++)
		{
			for (int i = 0; i < labels.size(); i++)
			{
				JsonList list = (JsonList) read(Integer.toString(labels.get(i)));
				list.set(it_count, new JsonNumber(V[i]));
			}
			V_prime = Matrix.multiply(M, V);
			V = V_prime;
		}
		return Status.DONE;
	}
}
