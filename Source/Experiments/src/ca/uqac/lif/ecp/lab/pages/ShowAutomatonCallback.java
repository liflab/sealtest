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
package ca.uqac.lif.ecp.lab.pages;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.ecp.lab.TestSuiteLab;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.parkbench.CommandRunner;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.LabAssistant;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.parkbench.server.ParkBenchCallback;

/**
 * Shows an automaton by calling GraphViz on the source file
 * @author Sylvain Hallé
 */
public class ShowAutomatonCallback extends ParkBenchCallback
{
	/**
	 * Determines if DOT is present on this system
	 */
	protected static final boolean s_dotPresent = checkForDot();
	
	public ShowAutomatonCallback(Laboratory lab, LabAssistant assistant) 
	{
		super("/data/fsm/image", lab, assistant);
	}

	@Override
	public CallbackResponse process(HttpExchange t) 
	{
		CallbackResponse response = new CallbackResponse(t);
		Map<String,String> params = getParameters(t);
		String name = params.get("name");
		if (name == null)
		{
			// Baaad request
			response.setCode(CallbackResponse.HTTP_BAD_REQUEST);
			return response;
		}
		String contents = FileHelper.internalFileToString(TestSuiteLab.class, TestSuiteLab.s_fsmPath + name);
		if (contents == null)
		{
			response.setCode(CallbackResponse.HTTP_NOT_FOUND);
			response.setContents("This graph cannot be found");
			return response;
		}
		if (!s_dotPresent)
		{
			response.setCode(CallbackResponse.HTTP_BAD_REQUEST);
			response.setContents("<!DOCTYPE html><html><body>GraphViz does not seem to be installed on this computer. The image cannot be generated. You can still view the <a href=\"/data/fsm/get?name=" + name + "\">source file</a>, though.");
			return response;
		}
		byte[] bytes = CommandRunner.runAndGet(new String[]{"dot", "-Tpng"}, contents);
		response.setContentType(CallbackResponse.ContentType.PNG);
		response.setContents(bytes);
		return response;
	}
	
	/**
	 * Checks if DOT is present on this system. This is done by attempting
	 * to run dot and to retrieve the exit code.
	 * @return true if present, false otherwise
	 */
	protected static boolean checkForDot()
	{
		CommandRunner cr = new CommandRunner(new String[]{"dot", "--help"});
		cr.start();
		int code = cr.getErrorCode();
		return code == 0 || code == 1;
	}

}
