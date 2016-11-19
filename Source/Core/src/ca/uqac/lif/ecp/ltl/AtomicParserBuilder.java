/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

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

import java.util.Scanner;
import java.util.Stack;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.BnfRule.InvalidRuleException;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicParserBuilder extends ParserBuilder<AtomicEvent>
{
	public AtomicParserBuilder()
	{
		super();
	}
	
	public AtomicParserBuilder(String expression)
	{
		super(expression);
	}
	
	public AtomicParserBuilder(Scanner scanner)
	{
		super(scanner);
	}
	
	@Override
	protected void setupParser(BnfParser parser) 
	{
		try 
		{
			// We add the definition of atom to the grammar
			// In this case, an atom is simply a string
			parser.addRule(BnfRule.parseRule("<atom> := ^\\w+"));
			//parser.setGrammar("<atom> := ^\\w+;");
		} 
		catch (InvalidRuleException e) 
		{
			// Should not happen
			e.printStackTrace();
		}
	}

	@Override
	protected Atom<AtomicEvent> parseAtom(ParseNode node, Stack<Operator<AtomicEvent>> stack)
	{
		Operator<AtomicEvent> op = stack.pop();
		if (op instanceof ParseNodeOperator)
		{
			ParseNodeOperator<AtomicEvent> p_node = (ParseNodeOperator<AtomicEvent>) op;
			String name = p_node.m_node.getToken();
			return new Atom<AtomicEvent>(new AtomicEvent(name));
		}
		return null;
	}

}
