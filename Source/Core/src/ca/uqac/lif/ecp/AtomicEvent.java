package ca.uqac.lif.ecp;

public class AtomicEvent extends Event 
{
	/**
	 * The event's label
	 */
	private final String m_label;
	
	/**
	 * Creates a new atomic event with given label
	 * @param label The label
	 */
	public AtomicEvent(String label)
	{
		super();
		m_label = label;
	}
	
	/**
	 * Gets the event's label
	 * @return The label
	 */
	public String getLabel()
	{
		return m_label;
	}
	
	@Override
	public String toString()
	{
		return m_label;
	}
	
	@Override
	public int hashCode()
	{
		return m_label.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		if (!(o instanceof AtomicEvent))
		{
			return false;
		}
		return m_label.compareTo(((AtomicEvent) o).m_label) == 0;
	}
}
