package ca.uqac.lif.ecp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uqac.lif.structures.MathSet;

public class GreedyTestSuiteFilter<T extends Event,U> extends TestSuiteFilter<T,U>
{
	public GreedyTestSuiteFilter(TriagingFunction<T,U> function)
	{
		super(function);
	}
	
	@Override
	public TestSuite<T> filterSuite(TestSuite<T> suite)
	{
		Set<MathSet<U>> covered = new HashSet<MathSet<U>>();
		TestSuite<T> new_suite = new TestSuite<T>();
		if (suite.size() <= 1)
		{
			return suite;
		}
		List<Trace<T>> ordered_suite = new ArrayList<Trace<T>>(suite.size());
		ordered_suite.addAll(suite);
		m_function.reset();
		for (T e : ordered_suite.get(0))
		{
			MathSet<U> category = m_function.read(e);
			covered.add(category);
		}
		new_suite.add(ordered_suite.get(0));
		boolean added = true;
		while (added)
		{
			added = false;
			int best = 0, best_index = 0, best_last_uncovered = 0;
			Set<MathSet<U>> best_covered = null;
			for (int i = 0; i < ordered_suite.size(); i++)
			{
				Trace<T> trace = ordered_suite.get(i);
				Set<MathSet<U>> t_covered = new HashSet<MathSet<U>>();
				MathSet<U> category = null;
				m_function.reset();
				int index = 0, last_uncovered = 0;
				for (T e : trace)
				{
					category = m_function.read(e);
					if (!covered.contains(category))
					{
						t_covered.add(category);
						last_uncovered = index;
					}
					index++;
				}
				if (t_covered.size() > best)
				{
					best = t_covered.size();
					best_index = i;
					best_covered = t_covered;
					best_last_uncovered = last_uncovered;
				}
			}	
			if (best > 0)
			{
				added = true;
				covered.addAll(best_covered);
				Trace<T> new_trace = ordered_suite.get(best_index).prefixTo(best_last_uncovered + 1);
				new_suite.add(new_trace);
				ordered_suite.remove(best_index);
			}
		}
		return new_suite;
	}
}
