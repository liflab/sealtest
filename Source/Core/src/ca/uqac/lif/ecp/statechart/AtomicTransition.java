package ca.uqac.lif.ecp.statechart;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.ecp.atomic.AtomicEvent;

public class AtomicTransition extends Transition<AtomicEvent> 
{
	/**
	 * The event this transitions is supposed to match
	 */
	protected AtomicEvent m_event;
	
	/**
	 * The target of this transition
	 */
	protected List<String> m_target;
	
	public AtomicTransition(AtomicEvent e)
	{
		super();
		m_event = e;
	}
	
	public AtomicTransition(AtomicEvent e, String ... target)
	{
		super();
		m_event = e;
		setTarget(target);
	}

	@Override
	public boolean matches(AtomicEvent event) 
	{
		return m_event.equals(event);
	}
	
	/**
	 * Sets the target of this transition
	 * @param target The target
	 */
	public void setTarget(List<String> target)
	{
		m_target = target;
	}
	
	public AtomicEvent getEvent()
	{
		return m_event;
	}
	
	/**
	 * Sets the target of this transition
	 * @param target The target
	 */
	public void setTarget(String ... target)
	{
		List<String> list = new ArrayList<String>(target.length);
		for (String i : target)
		{
			list.add(i);
		}
		setTarget(list);
	}

	@Override
	public List<String> getTarget() 
	{
		return m_target;
	}
	
	@Override
	public String toString()
	{
		return m_event + " -> " + m_target;
	}
}
