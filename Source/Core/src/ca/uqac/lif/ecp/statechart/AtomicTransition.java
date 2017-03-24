package ca.uqac.lif.ecp.statechart;

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
	protected StateNode<AtomicEvent> m_target;
	
	public AtomicTransition(AtomicEvent e)
	{
		super();
		m_event = e;
	}
	
	public AtomicTransition(AtomicEvent e, StateNode<AtomicEvent> target)
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
	public void setTarget(StateNode<AtomicEvent> target)
	{
		m_target = target;
	}
	
	public AtomicEvent getEvent()
	{
		return m_event;
	}

	@Override
	public StateNode<AtomicEvent> getTarget() 
	{
		return m_target;
	}
	
	@Override
	public String toString()
	{
		return m_event + " -> " + m_target;
	}
}
