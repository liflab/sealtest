package ca.uqac.lif.ecp.ltl;

import java.util.List;

import ca.uqac.lif.ecp.Event;
import ca.uqac.lif.structures.MathList;

/**
 * Ground term of an expression
 * @author Sylvain Hallé
 * @param <T>
 */
public abstract class GroundTerm<T extends Event> extends Operator<T>
{
	/**
	 * The event we've seen when we evaluated the atom
	 */
	protected EventLeaf<T> m_eventSeen = null;
	
	@Override
	public int size(boolean with_tree) 
	{
		return 1;
	}
	
	@Override
	public List<Operator<T>> getTreeChildren() 
	{
		MathList<Operator<T>> list = new MathList<Operator<T>>();
		if (m_eventSeen != null)
		{
			list.add(m_eventSeen);
		}
		return list;
	}
	
	@Override
	public void delete()
	{
		m_deleted = true;
		if (m_eventSeen != null)
		{
			m_eventSeen.delete();			
		}
	}
	
	@Override
	public void addOperand(Operator<T> op)
	{
		// Nothing to do
	}
	
	@Override
	public void clear()
	{
		super.clear();
		m_eventSeen = null;
	}
	
	@Override
	public void clean()
	{
		if (m_eventSeen != null && m_eventSeen.isDeleted())
		{
			m_eventSeen = null;
		}
	}



}
