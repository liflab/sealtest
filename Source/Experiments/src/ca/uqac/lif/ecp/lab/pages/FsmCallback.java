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

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.ecp.lab.TestSuiteLab;
import ca.uqac.lif.ecp.lab.fsm.AutomatonParser;
import ca.uqac.lif.parkbench.FileHelper;
import ca.uqac.lif.parkbench.LabAssistant;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.parkbench.server.CustomPageCallback;

public class FsmCallback extends CustomPageCallback
{
	public FsmCallback(Laboratory lab, LabAssistant assistant)
	{
		super("/data/fsm", lab, assistant);
	}
	
	@Override
	public String fill(String page, Map<String,String> params)
	{
		String out = page.replaceAll("\\{%TITLE%\\}", "FSM properties");
		out = out.replaceAll("\\{%FAVICON%\\}", getFavicon(IconType.GRAPH));
		String page_contents = FileHelper.internalFileToString(this.getClass(), "fsm-list.html");
		page_contents = page_contents.replaceAll("\\{%FSM_LIST%\\}", createFsmList().toString());
		out = out.replaceAll("\\{%CONTENT%\\}", page_contents.toString());
		return out;
	}
	
	public StringBuilder createFsmList()
	{
		StringBuilder out = new StringBuilder();
		out.append("<table>\n");
		List<String> listing = FileHelper.listAllFiles(TestSuiteLab.class.getResource(TestSuiteLab.s_fsmPath), ".*\\.dot");
		for (String filename : listing)
		{
			InputStream is = FileHelper.internalFileToStream(TestSuiteLab.class, TestSuiteLab.s_fsmPath + filename);
			Scanner scanner = new Scanner(is);
			AutomatonParser ap = new AutomatonParser(scanner);
			out.append("<tr>");
			out.append("<td><a href=\"/data/fsm/get?name=").append(filename).append("\">").append(ap.getTitle()).append("</a></td>\n");
			out.append("<td>[<a href=\"/data/fsm/image?name=").append(filename).append("\">image</a>]</td>\n");
			out.append("</tr>\n");
		}
		out.append("</table>");
		return out;
	}

}
