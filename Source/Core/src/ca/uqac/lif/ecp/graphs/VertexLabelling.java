package ca.uqac.lif.ecp.graphs;

import ca.uqac.lif.structures.MathMap;

/**
 * Labelling of a Graph. This is a mapping from a vertex ID (integer)
 * to objects of type <code>V</code>.
 * @author Sylvain Hallé
 *
 * @param <V> The type of the mapping
 */
public class VertexLabelling<V> extends MathMap<Integer,V>
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new empty vertex labelling
	 */
	public VertexLabelling()
	{
		super();
	}
}
