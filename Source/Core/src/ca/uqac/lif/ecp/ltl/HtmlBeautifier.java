package ca.uqac.lif.ecp.ltl;

import java.util.Stack;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator.Value;

public class HtmlBeautifier<T extends Event> extends HologramVisitor<T>
{
	/**
	 * Static variable to check if we are running in Windows.
	 * In such a case, Graphviz cannot display correctly Unicode characters,
	 * and we must fall-back to a more primitive ASCII representation. 
	 */
	protected static final transient boolean s_isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	
	/**
	 * The stack that will contain the parts of the string
	 */
	protected Stack<String> m_parts;
	
	public HtmlBeautifier()
	{
		super();
		m_parts = new Stack<String>();
	}
	
	public String getString()
	{
		if (m_parts.isEmpty())
		{
			return "";
		}
		String s = m_parts.peek();
		return s.substring(1, s.length() - 1);
	}
	
	/**
	 * Beautifies an operator
	 * @param op The operator
	 * @return The beautified string
	 */
	public String beautify(Operator<T> op)
	{
		HtmlBeautifier<T> hb = new HtmlBeautifier<T>();
		op.acceptPostfix(hb, false);
		return hb.getString();
	}
	
	public static String beautifyValue(Value v)
	{
		return beautifyValue(v, true);
	}
	
	public static String beautifyValue(Value v, boolean unicode)
	{
		if (unicode == false)
		{
			if (v == Value.TRUE)
			{
				return "1";
			}
			if (v == Value.FALSE)
			{
				return "0";
			}
			return "?";
		}
		if (v == Value.TRUE)
		{
			return "\u22a4";
		}
		if (v == Value.FALSE)
		{
			return "\u22a5";
		}
		return "?";
	}
	
	public static String beautifySymbol(Operator<?> op, boolean unicode)
	{
		if (unicode == false)
		{
			return beautifySymbol(op);
		}
		if (op instanceof And)
		{
			return "\u2227";
		}
		if (op instanceof Or)
		{
			return "\u2228";
		}
		if (op instanceof Not)
		{
			return "\u00ac";
		}
		if (op instanceof Implies)
		{
			return "\u2192";
		}
		if (op instanceof Globally)
		{
			return "G";
		}
		if (op instanceof Eventually)
		{
			return "F";
		}
		if (op instanceof Next)
		{
			return "X";
		}
		return op.getRootSymbol();
	}
	
	/**
	 * Prints a "beautified" symbol for an operator, for
	 * the purpose of displaying in the tree 
	 * @param op The operator
	 */
	public static String beautifySymbol(Operator<?> op)
	{
		if (op instanceof And)
		{
			if (s_isWindows)
			{
				return "&&";
			}
			return "&and;";
		}
		if (op instanceof Or)
		{
			if (s_isWindows)
			{
				return "||";
			}
			return "&or;";
		}
		if (op instanceof Not)
		{
			if (s_isWindows)
			{
				return "!";
			}
			return "&not;";
		}
		if (op instanceof Implies)
		{
			if (s_isWindows)
			{
				return "-&gt;";
			}
			return "&rarr;";
		}
		if (op instanceof Globally)
		{
			return "<B>G</B>";
		}
		if (op instanceof Eventually)
		{
			return "<B>F</B>";
		}
		if (op instanceof Next)
		{
			return "<B>X</B>";
		}
		return op.getRootSymbol();
	}

	@Override
	public void visit(Operator<T> op, int count)
	{
		StringBuilder expression = new StringBuilder();
		if (op instanceof NaryOperator)
		{
			NaryOperator<T> nop = (NaryOperator<T>) op;
			int num_children = nop.childrenCount();
			Stack<String> parts = new Stack<String>();
			for (int i = num_children - 1; i >= 0; i--)
			{
				// Must push children in stack, otherwise we
				// display them in reverse order
				parts.push(m_parts.pop());
				if (i > 0)
				{
					parts.push(" " + beautifySymbol(op) + " ");
				}
			}
			expression.append("(");
			while (!parts.isEmpty())
			{
				expression.append(parts.pop());
			}
			expression.append(")");
			m_parts.push(expression.toString());
			return;
		}
		else if (op instanceof UnaryOperator)
		{
			expression.append("(").append(beautifySymbol(op)).append(" ").append(m_parts.pop()).append(")");
			m_parts.push(expression.toString());
			return;
		}
		else if (op instanceof Atom)
		{
			expression.append(op.toString());
			m_parts.push(expression.toString());
			return;
		}
	}
}
