package ca.uqac.lif.ecp;

public class Matrix
{
	/**
	 * Computes the product of a matrix by a vector. In this method,
	 * <i>M</i> is a matrix and <i>V</i> is a column vector.
	 * NOTE: the number of columns of <i>M</i> must be equal to the number of
	 * lines of <i>V</i>. No check is made regarding this.
	 * @param M The matrix
	 * @param V The vector
	 * @return The result of computing <i>M</i>&times;<i>V</i>
	 */
	public static float[] multiply(float[][] M, float[] V)
	{
		float[] out = new float[V.length];
		for (int i = 0; i < V.length; i++)
		{
			out[i] = 0;
			for (int j = 0; j < M[i].length; j++)
			{
				out[i] += M[i][j] * V[j];
			}
		}
		return out;
	}
	
	/**
	 * Normalizes the columns of a matrix
	 * @param M The matrix
	 * @return The normalized matrix
	 */
	public static float[][] normalizeColumns(float[][] M)
	{
		float[][] out = new float[M.length][M[0].length];
		for (int j = 0; j < M[0].length; j++)
		{
			float col_total = 0;
			for (int i = 0; i < M.length; i++)
			{
				col_total += M[i][j];
			}
			if (col_total != 0)
			{
				for (int i = 0; i < M.length; i++)
				{
					out[i][j] = M[i][j] / col_total;
				}
			}
			else
			{
				for (int i = 0; i < M.length; i++)
				{
					out[i][j] = 0;
				}				
			}
		}
		return out;
	}
}
