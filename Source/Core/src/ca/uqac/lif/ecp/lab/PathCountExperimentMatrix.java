package ca.uqac.lif.ecp.lab;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.ecp.Matrix;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;

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
