package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.CayleyGraph;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.TriagingFunction;
import ca.uqac.lif.structures.MathSet;

public class HologramFunction<T extends Event> extends TriagingFunction<T, Operator<T>> 
{
	/**
	 * The transformation to apply to the hologram
	 */
	protected HologramTransformation<T> m_transformation;
	
	/**
	 * The LTL formula on which this function operates
	 */
	protected Operator<T> m_formula;
	
	/**
	 * Creates a new hologram triaging function
	 * @param formula The formula formula on which this function operates
	 * @param transformation The transformation to apply to the hologram
	 */
	public HologramFunction(Operator<T> formula, HologramTransformation<T> transformation)
	{
		super();
		m_formula = formula;
		m_transformation = transformation;
	}
	
	/**
	 * Creates a new hologram triaging function
	 * @param formula The formula formula on which this function operates
	 */
	public HologramFunction(Operator<T> formula)
	{
		this(formula, new IdentityHologramTransformation<T>());
	}

	@Override
	public MathSet<Operator<T>> read(T event) 
	{
		m_formula.evaluate(event);
		Operator<T> tree = m_formula.copy(true);
		Operator<T> hologram = m_transformation.transform(tree);
		return new MathSet<Operator<T>>(hologram);
	}

	@Override
	public MathSet<Operator<T>> getStartClass() 
	{
		return new MathSet<Operator<T>>();
	}

	@Override
	public CayleyGraph<T, Operator<T>> getCayleyGraph() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
