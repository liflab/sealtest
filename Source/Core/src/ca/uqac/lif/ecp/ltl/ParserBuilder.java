/*
    Log trace triaging and etc.
    Copyright (C) 2016-2020 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp.ltl;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor;
import ca.uqac.lif.bullwinkle.ParseNodeVisitor.VisitException;
import ca.uqac.lif.ecp.Event;

/**
 * Builds an LTL formula from an expression parsed from a grammar
 * @author Sylvain Hallé
 * @param <T> The type of the events
 */
public abstract class ParserBuilder<T extends Event> extends OperatorBuilder<T> 
{
	protected String m_expression;
	
	protected final BnfParser m_parser = initializeParser();
	
	public ParserBuilder()
	{
		super();
		m_expression = null;
		setupParser(m_parser);
	}
	
	public ParserBuilder(String expression)
	{
		super();
		m_expression = expression;
		setupParser(m_parser);
	}
	
	public ParserBuilder(Scanner scanner)
	{
		super();
		m_expression = scanString(scanner);
		setupParser(m_parser);
	}

	@Override
	public Operator<T> build() throws BuildException
	{
		try 
		{
			ParseNode node = m_parser.parse(m_expression);
			if (node == null)
			{
				throw new BuildException("The parser could not parse the expression " + m_expression);
			}
			LtlVisitor visitor = new LtlVisitor();
			node.postfixAccept(visitor);
			return visitor.getOperator();
		}
		catch (VisitException e) 
		{
			throw new BuildException(e.getMessage());
		}
		catch (ParseException e) 
		{
			throw new BuildException(e.getMessage());
		}
	}
	
	/**
	 * Visits a parse tree and builds an LTL expression
	 */
	protected class LtlVisitor implements ParseNodeVisitor
	{
		/**
		 * The stack of sub-expressions already built
		 */
		Stack<Operator<T>> m_stack;
		
		/**
		 *  Creates a new visitor
		 */
		LtlVisitor()
		{
			super();
			m_stack = new Stack<Operator<T>>();
		}
		
		/**
		 * Gets the expression built by this visitor
		 * @return The expression
		 */
		public Operator<T> getOperator()
		{
			if (m_stack.isEmpty())
			{
				return null;
			}
			return m_stack.peek();
		}

		@Override
		public void pop() 
		{
			// Nothing to do
		}

		@Override
		public void visit(ParseNode node)
		{
			String token_name = node.getToken();
			if (token_name.compareTo("<and>") == 0)
			{
				And<T> o = new And<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<or>") == 0)
			{
				Or<T> o = new Or<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<implies>") == 0)
			{
				Implies<T> o = new Implies<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<globally>") == 0)
			{
				Globally<T> o = new Globally<T>();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<eventually>") == 0)
			{
				Eventually<T> o = new Eventually<T>();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<next>") == 0)
			{
				Next<T> o = new Next<T>();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<not>") == 0)
			{
				Not<T> o = new Not<T>();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<until>") == 0)
			{
				Until<T> o = new Until<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<weak_until>") == 0)
			{
				WeakUntil<T> o = new WeakUntil<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<release>") == 0)
			{
				Release<T> o = new Release<T>();
				Operator<T> right = m_stack.pop();
				Operator<T> left = m_stack.pop();
				o.addOperand(left);
				o.addOperand(right);
				m_stack.push(o);
			}
			else if (token_name.compareTo("<atom>") == 0)
			{
				// This is an atom
				Atom<T> atom = parseAtom(node, m_stack);
				m_stack.push(atom);
			}
			else if (token_name.compareTo("<S>") == 0 || token_name.compareTo("(") == 0 
					|| token_name.compareTo(")") == 0 || token_name.compareTo("&") == 0 
					|| token_name.compareTo("|") == 0 || token_name.compareTo("G") == 0 
					|| token_name.compareTo("X") == 0 || token_name.compareTo("F") == 0 
					|| token_name.compareTo("U") == 0 || token_name.compareTo("!") == 0
					|| token_name.compareTo("W") == 0 || token_name.compareTo("R") == 0
					|| token_name.compareTo("->") == 0 || token_name.compareTo("<binary-op>") == 0
					|| token_name.compareTo("<unary-op>") == 0)
			{
				// Ignore symbols
			}
			else
			{
				m_stack.push(new ParseNodeOperator<T>(node));
			}
		}
	}
	
	/**
	 * Sets up the parser for LTL
	 * @return An instance of the parser
	 */
	protected static BnfParser initializeParser()
	{
		BnfParser parser = new BnfParser();
		String grammar = scanString(new Scanner(ParserBuilder.class.getResourceAsStream("ltl.bnf")));
		try 
		{
			parser.setGrammar(grammar);
		} 
		catch (InvalidGrammarException e)
		{
			// Should not happen
			e.printStackTrace();
		}
		return parser;
	}
	
	/**
	 * Utility method to create a string from a scanner
	 * @param scanner The scanner
	 * @return The resulting string
	 */
	protected static String scanString(Scanner scanner)
	{
		StringBuilder out = new StringBuilder();
		while (scanner.hasNextLine())
		{
			out.append(scanner.nextLine()).append("\n");
		}
		return out.toString();
	}
	
	/**
	 * Sets up the parser for this specific operator builder.
	 * Normally this method should add at least the definition of the
	 * non-terminal symbol &lt;atom&gt; to the grammar, and possibly
	 * other rules.
	 * @param parser The parser
	 */
	protected abstract void setupParser(BnfParser parser);
	
	/**
	 * Creates an atom from the parse stack and the current parse node
	 * @param node The node
	 * @param stack The stack
	 * @return The atom
	 */
	protected abstract Atom<T> parseAtom(ParseNode node, Stack<Operator<T>> stack);
	
	/**
	 * Operator simply meant to contain a parse node
	 */
	protected static class ParseNodeOperator<U extends Event> extends Operator<U>
	{
		/**
		 * The parse node this operator carries
		 */
		public ParseNode m_node;
		
		public ParseNodeOperator(ParseNode node)
		{
			super();
			m_node = node;
		}

		@Override
		public Operator<U> copy(boolean with_tree) { return null; }

		@Override
		public void evaluate(U event) {	}

		@Override
		public void delete() {	}

		@Override
		public int size(boolean with_tree) { return 0; }

		@Override
		public String getRootSymbol() {	return null; }

		@Override
		public void acceptPrefix(HologramVisitor<U> visitor, boolean in_tree) {	}
		
		@Override
		public void acceptPostfix(HologramVisitor<U> visitor, boolean in_tree) { }

		@Override
		public List<Operator<U>> getTreeChildren() { return null; }

		@Override
		public void addOperand(Operator<U> op) { }
		
		@Override
		public void clean() { }
		
		@Override
		public String toString()
		{
			return m_node.toString();
		}
	}
}
