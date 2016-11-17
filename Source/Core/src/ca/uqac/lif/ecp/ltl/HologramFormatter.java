package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.CayleyGraph.LabelFormatter;
import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathSet;

public class HologramFormatter<T extends Event> extends LabelFormatter<Operator<T>>
{
	protected final transient HologramHasher<T> m_hasher = new HologramHasher<T>();
	
	@Override
	public String format(MathSet<Operator<T>> category)
	{
		for (Operator<T> cat : category)
		{
			String out = m_hasher.getHash(cat);
			return out;
		}
		return "";
	}
}
