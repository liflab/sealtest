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
package ca.uqac.lif.ecp;

import java.util.ArrayList;

import ca.uqac.lif.ecp.graphs.BreadthFirstVisitor;
import ca.uqac.lif.ecp.lab.GraphExperiment;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonNumber;

public class CountVisitor<T extends Event,U extends Object> extends BreadthFirstVisitor<T>
{
	protected GraphExperiment m_experiment;
	
	public CountVisitor(GraphExperiment experiment)
	{
		super();
		m_experiment = experiment;
	}
	
	public void start(CayleyGraph<T,U> g, int start_id, int max_depth)
	{
		super.start(g, start_id, max_depth);
	}

	@Override
	public void visit(ArrayList<Edge<T>> path)
	{
		int length = path.size();
		Edge<T> last_edge = path.get(path.size() - 1);
		int last_label = last_edge.getDestination();
		JsonList list = m_experiment.readList(Integer.toString(last_label));
		JsonNumber j_num = (JsonNumber) list.get(length);
		list.set(length, new JsonNumber(j_num.numberValue().intValue() + 1));
	}
	
	@Override
	public void depthStep(int depth)
	{
		float progression = (float) depth / m_experiment.readFloat("max-length");
		System.out.println("DEPTH " + depth + " " + progression);
		m_experiment.setProgression(progression); 
	}
}
