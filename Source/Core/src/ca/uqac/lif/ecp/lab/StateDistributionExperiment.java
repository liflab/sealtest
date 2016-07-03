package ca.uqac.lif.ecp.lab;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ca.uqac.lif.ecp.Matrix;
import ca.uqac.lif.json.JsonList;

public class StateDistributionExperiment extends GraphExperiment 
{
	public StateDistributionExperiment(Scanner scanner, int iterations)
	{
		super(scanner);
		setDescription("This experiment counts the proportion of paths of length n ending in each state of the automaton for the property '" + readString("property") + "', when n -> infty");
		describe("iterations", "Number of iterations to be done for the matrix multiplication");
		setInput("iterations", iterations);
		write("iterations", iterations);
		JsonList list = new JsonList();
		JsonList out = new JsonList();
		List<Integer> labels = m_graph.getVertexLabels();
		Collections.sort(labels);
		for (int label : labels)
		{
			list.add(label);
			out.add(0);
		}
		describe("state", "Each state of the automaton");
		write("state", list);
		describe("fraction", "The fraction of paths ending in each state of the automaton");
		write("fraction", out);
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
		float[][] M_norm = Matrix.normalizeColumns(M);
		// Create empty vector, put 1 as its first component
		float[] V = new float[M_norm[0].length];
		for (int i = 0; i < V.length; i++)
		{
			V[i] = 0;
		}
		V[0] = 1;
		// Repeatedly multiply M by V 
		float[] V_prime = null;
		for (int i = 0; i < readInt("iterations"); i++)
		{
			V_prime = Matrix.multiply(M_norm, V);
			V = V_prime;
		}
		// Write results
		JsonList list = new JsonList();
		for (int i = 0; i < V.length; i++)
		{
			list.add(V[i]);
		}
		write("fraction", list);
		return Status.DONE;
	}
}
