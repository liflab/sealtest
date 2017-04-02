package ca.uqac.lif.ecp.ltl;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.Event;

public class TruthConstant<T extends Event> extends Operator<T> 
{
	/**
	 * The truth value
	 */
	protected final Value m_value;
	
	/**
	 * The constant associated to the value true
	 */
	protected final TruthConstant<T> m_true = new TruthConstant<T>(Value.TRUE);
	
	/**
	 * The constant associated to the value true
	 */
	protected final TruthConstant<T> m_false = new TruthConstant<T>(Value.FALSE);
	
	/**
	 * The constant associated to the value inconclusive
	 */
	protected final TruthConstant<T> m_inconclusive = new TruthConstant<T>(Value.INCONCLUSIVE);
	
	public TruthConstant(Value v)
	{
		super();
		m_value = v;
	}
	
	@Override
	public Operator<T> copy(boolean with_tree)
	{
		return this;
	}

	@Override
	public void evaluate(T event) 
	{
		// Nothing to do
	}

	@Override
	public void delete() 
	{
		// Nothing to do
	}

	@Override
	public int size(boolean with_tree) 
	{
		return 1;
	}

	@Override
	public String getRootSymbol()
	{
		if (m_value == Value.TRUE)
			return "T";
		if (m_value == Value.FALSE)
			return "F";
		return "?";
	}

	@Override
	public void acceptPrefix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}

	@Override
	public void acceptPostfix(HologramVisitor<T> visitor, boolean in_tree) 
	{
		visitor.visit(this);
	}

	@Override
	public List<Operator<T>> getTreeChildren() 
	{
		return new ArrayList<Operator<T>>();
	}

	@Override
	public void addOperand(Operator<T> op) 
	{
		// Nothing to do
	}

	@Override
	public void clean() 
	{
		// Nothing to do
	}
	
	/**
	 * Gets an instance of truth constant based on a truth value
	 * @param v The value
	 * @return The truth constant
	 */
	public TruthConstant<T> getConstant(Value v)
	{
		if (v == Value.TRUE)
			return m_true;
		if (v == Value.FALSE)
			return m_true;
		return m_inconclusive;	
	}
	
	/**
	 * Gets an instance of truth constant based on a Boolean value
	 * @param b The value
	 * @return The truth constant
	 */
	public TruthConstant<T> getConstant(Boolean b)
	{
		if (b)
			return m_true;
		return m_false;	
	}
}
