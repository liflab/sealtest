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
package ca.uqac.lif.ecp.atomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uqac.lif.ecp.Alphabet;
import ca.uqac.lif.ecp.Edge;
import ca.uqac.lif.ecp.graphs.Vertex;

public class GffAutomatonParser implements AutomatonParser
{
	/**
	 * The regex pattern matching the proposition tag
	 */
	protected static final transient Pattern s_atomPattern = Pattern.compile("<Prop.*?>(.*?)</Prop.*?>", Pattern.CASE_INSENSITIVE);

	/**
	 * The regex pattern matching the source state tag
	 */
	protected static final transient Pattern s_fromPattern = Pattern.compile("<From>(.*?)</From>", Pattern.CASE_INSENSITIVE);

	/**
	 * The regex pattern matching the source state tag
	 */
	protected static final transient Pattern s_toPattern = Pattern.compile("<To>(.*?)</To>", Pattern.CASE_INSENSITIVE);

	/**
	 * The regex pattern matching the label tag (first variant)
	 */
	protected static final transient Pattern s_labelPattern = Pattern.compile("<Label>(.*?)</Label>", Pattern.CASE_INSENSITIVE);
	
	/**
	 * The regex pattern matching the label tag (second variant)
	 */
	protected static final transient Pattern s_readPattern = Pattern.compile("<read>(.*?)</read>", Pattern.CASE_INSENSITIVE);

	/**
	 * The regex pattern matching the initial state ID tag
	 */
	protected static final transient Pattern s_idPattern = Pattern.compile("<StateID>(.*?)</StateID>", Pattern.CASE_INSENSITIVE);

	/**
	 * The regex pattern matching the state ID attribute
	 */
	protected static final transient Pattern s_statePattern = Pattern.compile("<State sid=\"(.*?)\">", Pattern.CASE_INSENSITIVE);

	/**
	 * The main sections of the XML document
	 */
	protected static enum Element {TRANSITION, INITIAL, ALPHABET, NOTHING, FORMULA}

	/**
	 * A map between atomic symbols and atomic events
	 */
	protected transient Map<String,AtomicEvent> m_events;

	/**
	 * A counter to give unique names to events
	 */
	protected int m_labelCnt = 0;

	/**
	 * Creates a new instance of the parser.
	 */
	public GffAutomatonParser()
	{
		super();
		m_events = new HashMap<String,AtomicEvent>();
	}

	@Override
	public Automaton parse(Scanner scanner, String title)
	{
		Element section = Element.NOTHING;
		Automaton aut = new Automaton();
		int to = -1, from = -1, initial = -1;
		String label = "", formula = "";
		List<String> alphabet = new ArrayList<String>();
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.matches("</Transition>") || line.matches("</transition>") ||
					line.matches("</Alphabet>") || line.matches("</alphabet>") ||  
					line.matches("</InitialStateSet>") || line.matches("</initialStateSet>") ||
					line.matches("</Formula>") || line.matches("</formula>"))
			{
				section = Element.NOTHING;
				continue;
			}
			if (line.startsWith("<State sid") || line.startsWith("<state sid"))
			{
				Matcher m_sid = s_statePattern.matcher(line);
				if (m_sid.matches())
				{
					int id = Integer.parseInt(m_sid.group(1).trim());
					aut.add(new Vertex<AtomicEvent>(id));
				}
			}
			if (line.startsWith("<Transition tid") || line.startsWith("<transition tid"))
			{
				section = Element.TRANSITION;
				to = -1;
				from = -1;
				label = "";
				continue;
			}
			if (line.startsWith("<Alphabet") || line.startsWith("<alphabet"))
			{
				section = Element.ALPHABET;
				continue;
			}
			if (line.matches("<Formula>"))
			{
				section = Element.FORMULA;
				continue;
			}
			if (line.matches("<InitialStateSet>") || line.matches("<initialStateSet>"))
			{
				section = Element.INITIAL;
				continue;
			}
			if (section == Element.TRANSITION)
			{
				Matcher m_to = s_toPattern.matcher(line);
				if (m_to.matches())
				{
					to = Integer.parseInt(m_to.group(1).trim());
				}
				Matcher m_from = s_fromPattern.matcher(line);
				if (m_from.matches())
				{
					from = Integer.parseInt(m_from.group(1).trim());
				}
				Matcher m_label = s_labelPattern.matcher(line);
				if (m_label.matches())
				{
					label = m_label.group(1).trim();
				}
				Matcher m_read = s_readPattern.matcher(line);
				if (m_read.matches())
				{
					label = m_read.group(1).trim();
				}
				if (to >= 0 && from >= 0 && !label.isEmpty())
				{
					Edge<AtomicEvent> e = new Edge<AtomicEvent>(from, getLabel(label), to);
					aut.getVertex(from).add(e);
				}
			}
			if (section == Element.ALPHABET)
			{
				Matcher m_atom = s_atomPattern.matcher(line);
				if (m_atom.matches())
				{
					alphabet.add(m_atom.group(1).trim());
				}
			}
			if (section == Element.FORMULA)
			{
				formula += line;
			}
			if (section == Element.INITIAL && initial < 0)
			{
				Matcher m_id = s_idPattern.matcher(line);
				if (m_id.matches())
				{
					initial = Integer.parseInt(m_id.group(1).trim());
				}
			}
		}
		Alphabet<AtomicEvent> a_alphabet = new Alphabet<AtomicEvent>();
		a_alphabet.addAll(m_events.values());
		aut.setAlphabet(a_alphabet);
		aut.setInitialVertexId(initial);
		aut.setTitle(formula);
		aut.makeTotal();
		return aut;
	}

	@Override
	public Automaton parse(String content, String title)
	{
		return parse(new Scanner(content), title);
	}

	protected AtomicEvent getLabel(String labels)
	{
		if (labels.compareToIgnoreCase("true") == 0)
		{
			return ElseEvent.instance;
		}
		if (!m_events.containsKey(labels))
		{
			AtomicEvent e = new AtomicEvent("e" + m_labelCnt);
			m_labelCnt++;
			m_events.put(labels, e);
			return e;
		}
		return m_events.get(labels);
	}

	@Override
	public void reset()
	{
		m_events.clear();
		m_labelCnt = 0;
	}
}
