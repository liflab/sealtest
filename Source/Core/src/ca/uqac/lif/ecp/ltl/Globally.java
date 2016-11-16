package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

public class Globally<T extends Event> extends UnaryTemporalOperator<T> 
{	
	public Globally()
	{
		super("G");
	}
	
	public Globally(Operator<T> operand)
	{
		super("G", operand);
	}

	@Override
	public void evaluate(T event) 
	{
		Operator<T> new_operand = m_operand.copy(false);
		m_instantiatedTrees.add(new_operand);
		boolean false_seen = false;
		for (Operator<T> op : m_instantiatedTrees)
		{
			op.evaluate(event);
			Value v = op.getValue();
			if (v == Value.FALSE)
			{
				false_seen = true;
			}
		}
		if (false_seen)
		{
			m_value = Value.FALSE;
			return;
		}
		m_value = Value.INCONCLUSIVE;
	}

	@Override
	public Globally<T> copy(boolean with_tree) 
	{
		Globally<T> g = new Globally<T>();
		super.copyInto(g, with_tree);
		return g;
	}
}
