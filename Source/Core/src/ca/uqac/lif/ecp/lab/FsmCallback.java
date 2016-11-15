package ca.uqac.lif.ecp.lab;

import java.util.Map;

import ca.uqac.lif.parkbench.LabAssistant;
import ca.uqac.lif.parkbench.Laboratory;
import ca.uqac.lif.parkbench.server.TemplatePageCallback;
import ca.uqac.lif.parkbench.server.TemplatePageCallback.IconType;

public class FsmCallback extends TemplatePageCallback
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
		/*
		if (Plot.isGnuplotPresent())
		{
			out = out.replaceAll("\\{%PLOTS%\\}", getPlots());
			if (AllPlotsCallback.s_pdftkPresent)
			{
				out = out.replaceAll("\\{%ALL_PLOTS%\\}", "<p><a class=\"btn btn-all-plots\" title=\"Download all plots as a single PDF file\" href=\"all-plots\">Download all plots</a></p>");
			}
		}
		else
		{
			out = out.replaceAll("\\{%PLOTS%\\}", "<p>Gnuplot was not detected on this system. The plot functionality is disabled.</p>");
		}*/
		out = out.replaceAll("\\{%SEL_PLOTS%\\}", "selected");
		return out;
	}

}
