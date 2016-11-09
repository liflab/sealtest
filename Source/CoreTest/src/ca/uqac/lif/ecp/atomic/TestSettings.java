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
package ca.uqac.lif.ecp.atomic;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

public class TestSettings
{
	/**
	 * The folder where the test data is located
	 */
	public static String s_dataFolder = "data/";
	
	@Test
	public void dummyTest()
	{
		
	}
	
	public static Automaton loadAutomaton(String filename)
	{
		 InputStream is = TestSettings.class.getResourceAsStream(s_dataFolder + filename);
		 Scanner scanner = new Scanner(is);
		 return Automaton.parseDot(scanner);
	}
	
}
