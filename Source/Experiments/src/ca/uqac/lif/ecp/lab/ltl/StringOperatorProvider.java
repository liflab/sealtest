package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.atomic.AtomicEvent;
import ca.uqac.lif.ecp.ltl.AtomicParserBuilder;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.ecp.ltl.OperatorBuilder.BuildException;
import ca.uqac.lif.parkbench.Experiment;

public class StringOperatorProvider implements OperatorProvider<AtomicEvent> 
{
	public static final transient String FORMULA = "Formula";
	
	protected Operator<AtomicEvent> m_operator = null;
	
	public StringOperatorProvider(String s)
	{
		super();
		AtomicParserBuilder m_builder = new AtomicParserBuilder(s);
		Operator<AtomicEvent> op;
		try 
		{
			op = m_builder.build();
			m_operator = op;
		} 
		catch (BuildException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Operator<AtomicEvent> getOperator()
	{
		return m_operator;
	}

	@Override
	public void write(Experiment e)
	{
		e.describe(FORMULA, "The LTL formula used as the input");
		e.setInput(FORMULA, m_operator.toString());
		
	}

}
