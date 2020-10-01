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

import java.util.Scanner;

/**
 * Parses an automaton from a static source, such as a file or a string.
 */
public interface AutomatonParser
{
	/**
	 * Parses an automaton.
	 * @param scanner A scanner open on a source of text lines
	 * @param title A title given to the automaton
	 * @return The automaton, or <tt>null</tt> if no automaton could be
	 * parsed
	 */
	public Automaton parse(Scanner scanner, String title);
	
	/**
	 * Parses an automaton.
	 * @param contents A string of text lines with the content of the automaton
	 * to be parsed
	 * @param title A title given to the automaton
	 * @return The automaton, or <tt>null</tt> if no automaton could be
	 * parsed
	 */
	public Automaton parse(String content, String title);
	
	/**
	 * Resets the parser. This puts the object in a state where it is ready to
	 * parse a new input from scratch.
	 */
	public void reset();
}
