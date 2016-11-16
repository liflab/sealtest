package ca.uqac.lif.ecp.ltl;

import java.util.Stack;

import ca.uqac.lif.ecp.Event;

public abstract class HologramVisitor<T extends Event> 
{
	private int m_idCounter = 0;
	
	private Stack<Integer> m_idStack;
	
	public HologramVisitor()
	{
		super();
		m_idCounter = 0;
		m_idStack = new Stack<Integer>();
	}
	
	public final void visit(Operator<T> op)
	{
		int cnt = m_idCounter;
		m_idStack.push(m_idCounter);
		m_idCounter++;
		visit(op, cnt);
	}
	
	public final void visit(T event)
	{
		int cnt = m_idCounter;
		m_idStack.push(m_idCounter);
		m_idCounter++;
		visit(event, cnt);		
	}
	
	public final void backtrack()
	{
		m_idStack.pop();
		if (!m_idStack.isEmpty())
		{
			backtrack(m_idStack.peek());
		}
	}
	
	public abstract void visit(Operator<T> op, int count);
	
	public abstract void visit(T event, int count);
	
	public void backtrack(int count)
	{
		// Do nothing
	}
}
