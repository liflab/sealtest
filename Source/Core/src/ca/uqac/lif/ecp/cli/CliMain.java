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
package ca.uqac.lif.ecp.cli;

import java.lang.reflect.Array;

import ca.uqac.lif.ecp.cli.CliParser.Argument;
import ca.uqac.lif.ecp.cli.CliParser.ArgumentMap;

/**
 * Main class to use the library as a stand-alone command-line program.
 * <p>
 * Usage:
 * <pre>
 * java -jar seal-test.jar [action] [params]
 * </pre>
 * 
 * @author Sylvain Hallé
 */
public class CliMain
{
	/* Return codes */
	public static final transient int ERR_OK = 0;
	public static final transient int ERR_ARGUMENTS = 1;
	
	/**
	 * The version number of the library
	 */
	public static final transient String s_versionString = "1.0";
	
	public static void main(String[] args)
	{
		System.err.println("Seal Test v" + s_versionString + " - A test generator with many tricks");
		System.err.println("(C) 2016 Laboratoire d'informatique formelle, Université du Québec à Chicoutimi");
		System.err.println("https://liflab.github.io/seal-test\n");
		if (args.length < 1)
		{
			System.err.println("Invalid number of arguments");
			System.err.println("Usage: java -jar seal-test.jar [action] [parameters]");
			System.err.println("Where action is \"generate\" or \"coverage\"");
			System.err.println("Run java -jar seal-test.jar action --help for help about each option");
			System.exit(ERR_ARGUMENTS);
		}
		String action = args[0];
		if (action.compareToIgnoreCase("generate") == 0)
		{
			CliParser cli_parser = new CliParser();
			cli_parser.addArgument(new Argument().withShortName("h").withLongName("help").withDescription("Show usage"));
			cli_parser.addArgument(new Argument().withShortName("s").withLongName("spec").withArgument("file").withDescription("Reads specification from file"));
			cli_parser.addArgument(new Argument().withShortName("f").withLongName("function").withArgument("name").withDescription("Use triaging function name"));
			ArgumentMap arguments = cli_parser.parse(removeAction(args));
			if (arguments.hasOption("help"))
			{
				cli_parser.printHelp("", System.err);
				System.exit(ERR_OK);
			}
		}
		else if (action.compareToIgnoreCase("coverage") == 0)
		{
			CliParser cli_parser = new CliParser();
			cli_parser.addArgument(new Argument().withShortName("h").withLongName("help").withDescription("Show usage"));
			ArgumentMap arguments = cli_parser.parse(removeAction(args));
			if (arguments.hasOption("help"))
			{
				cli_parser.printHelp("", System.err);
				System.exit(ERR_OK);
			}
		}
		else
		{
			System.err.println("Incorrect action");
			System.exit(ERR_ARGUMENTS);
		}
		System.exit(ERR_OK);
	}
	
	protected static String[] removeAction(String[] args)
	{
		String[] out = new String[args.length - 1];
		for (int i = 1; i < args.length; i++)
		{
			out[i - 1] = args[i];
		}
		return out;
	}
}
