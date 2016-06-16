package ca.uqac.lif.brutecount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CountVisitor extends BreadthFirstVisitor
{
	public HashMap<String,Integer>[] m_counts;
	
	@SuppressWarnings("unchecked")
	public void start(Graph g, String start_label, int max_depth)
	{
		m_counts = new HashMap[max_depth + 1];
		super.start(g, start_label, max_depth);
	}

	@Override
	public void visit(ArrayList<Edge> path)
	{
		int length = path.size();
		Edge last_edge = path.get(path.size() - 1);
		String last_label = last_edge.m_destination;
		HashMap<String,Integer> map = m_counts[length];
		if (map == null)
		{
			m_counts[length] = new HashMap<String,Integer>();
			map = m_counts[length];
		}
		if (!map.containsKey(last_label))
		{
			map.put(last_label, 0);
		}
		int value = map.get(last_label);
		map.put(last_label, value + 1);
	}
	
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		for (int i = 1; i < m_counts.length; i++)
		{
			HashMap<String,Integer> map = m_counts[i];
			Set<String> keys = map.keySet();
			List<String> sorted_keys = new ArrayList<String>();
			sorted_keys.addAll(keys);
			Collections.sort(sorted_keys);
			out.append(i);
			for (String s : sorted_keys)
			{
				out.append(",").append(map.get(s));
			}
			out.append("\n");
		}
		return out.toString();
	}

}
