package ca.uqac.lif.ecp.lab.ltl;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.ecp.ltl.Operator;
import ca.uqac.lif.parkbench.Experiment;

public interface OperatorProvider<T extends Event> 
{
	public Operator<T> getOperator();
	
	public void write(Experiment e);
}
