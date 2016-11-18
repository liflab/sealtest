package ca.uqac.lif.ecp.ltl;

import ca.uqac.lif.ecp.Event;

/**
 * Creates a string "hash" from an evaluation tree. The hash corresponds
 * to symboles denoting the nodes encountered along a prefix traversal
 * of the evaluation tree.
 * @author Sylvain Hallé
 *
 * @param <T> The event type
 */
public class HologramHasher<T extends Event> extends HologramVisitor<T>
{
	StringBuilder m_buffer;

	int m_deletedDepth = 0;
	
	boolean m_showTruthValue = true;

	public HologramHasher()
	{
		super();
		m_buffer = new StringBuilder();
	}

	public String getHash(Operator<T> op)
	{
		HologramHasher<T> h = new HologramHasher<T>();
		op.acceptPrefix(h, true);
		String buf = h.m_buffer.toString();
		buf = trimLastArrows(buf.substring(1));
		return buf;
	}

	@Override
	public void visit(Operator<T> op, int count) 
	{
		if (op.isDeleted())
		{
			m_deletedDepth++;
		}
		else
		{
			m_buffer.append("\u2193"); // Down arrow
			m_buffer.append(HtmlBeautifier.beautifySymbol(op, true));
			if (m_showTruthValue)
			{
				m_buffer.append(HtmlBeautifier.beautifyValue(op.getValue(), true));
			}
		}
	}

	@Override
	public void backtrack(int count)
	{
		if (m_deletedDepth == 0)
		{	
			m_buffer.append("\u2191"); // Up arrow
		}
		else
		{
			m_deletedDepth--;
		}
	}

	protected static String trimLastArrows(String s)
	{
		for (int i = s.length() - 1; i >= 0; i--)
		{
			String character = s.substring(i, i + 1);
			if (character.compareTo("\u2191") != 0)
			{
				return s.substring(0, i + 1);
			}
		}
		return s;
	}
}
