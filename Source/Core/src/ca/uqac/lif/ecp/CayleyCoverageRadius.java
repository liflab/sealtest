package ca.uqac.lif.ecp;

import java.util.Map;
import java.util.Set;

import ca.uqac.lif.ecp.CayleyCoverageRadius.RadiusMap;
import ca.uqac.lif.structures.MathMap;
import ca.uqac.lif.structures.MathSet;

public class CayleyCoverageRadius<T extends Event,U> extends CayleyCoverageMetric<T,U,RadiusMap> 
{
	/**
	 * Up to what depth to compute the radius
	 */
	protected int m_depth;

	public CayleyCoverageRadius(CayleyGraph<T, U> graph, TriagingFunction<T, U> function) 
	{
		super(graph, function);
	}
	
	public void setDepth(int depth)
	{
		m_depth = depth;
	}

	public static class RadiusMap extends MathMap<Integer,Float>
	{
		/**
		 * Dummy UID
		 */
		private static final long serialVersionUID = 1L;
	}

	@Override
	public RadiusMap getCoverage(Set<Trace<T>> traces) 
	{
		RadiusMap map = new RadiusMap();
		Map<Integer,Set<MathSet<U>>> classes_by_depth = m_graph.getClassesByDepth(m_depth);
		return map;
	}
	

}
