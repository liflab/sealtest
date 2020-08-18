package ca.uqac.lif.ecp;

public abstract class TestSuiteFilter<T extends Event,U>
{
	protected TriagingFunction<T,U> m_function;
	
	public TestSuiteFilter(TriagingFunction<T,U> function)
	{
		super();
		m_function = function;
	}
	
	public abstract TestSuite<T> filterSuite(TestSuite<T> suite);
}
