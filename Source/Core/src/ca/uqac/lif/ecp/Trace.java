package ca.uqac.lif.ecp;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Trace<T> implements List<T> 
{
	/**
	 * The inner list used to contain the events
	 */
	private List<T> m_innerList;
	
	public Trace()
	{
		super();
		m_innerList = new LinkedList<T>();
	}
	
	public Trace(Trace<T> t)
	{
		this();
		addAll(t);
	}
	
	public void add(int arg0, T arg1) {
		m_innerList.add(arg0, arg1);
	}

	public boolean add(T arg0) {
		return m_innerList.add(arg0);
	}

	public boolean addAll(Collection<? extends T> arg0) {
		return m_innerList.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return m_innerList.addAll(arg0, arg1);
	}

	public void clear() {
		m_innerList.clear();
	}

	public boolean contains(Object arg0) {
		return m_innerList.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		return m_innerList.containsAll(arg0);
	}

	public void forEach(Consumer<? super T> arg0) {
		m_innerList.forEach(arg0);
	}

	public T get(int arg0) {
		return m_innerList.get(arg0);
	}

	public int indexOf(Object arg0) {
		return m_innerList.indexOf(arg0);
	}

	public boolean isEmpty() {
		return m_innerList.isEmpty();
	}

	public Iterator<T> iterator() {
		return m_innerList.iterator();
	}

	public int lastIndexOf(Object arg0) {
		return m_innerList.lastIndexOf(arg0);
	}

	public ListIterator<T> listIterator() {
		return m_innerList.listIterator();
	}

	public ListIterator<T> listIterator(int arg0) {
		return m_innerList.listIterator(arg0);
	}

	public Stream<T> parallelStream() {
		return m_innerList.parallelStream();
	}

	public T remove(int arg0) {
		return m_innerList.remove(arg0);
	}

	public boolean remove(Object arg0) {
		return m_innerList.remove(arg0);
	}

	public boolean removeAll(Collection<?> arg0) {
		return m_innerList.removeAll(arg0);
	}

	public boolean removeIf(Predicate<? super T> arg0) {
		return m_innerList.removeIf(arg0);
	}

	public void replaceAll(UnaryOperator<T> arg0) {
		m_innerList.replaceAll(arg0);
	}

	public boolean retainAll(Collection<?> arg0) {
		return m_innerList.retainAll(arg0);
	}

	public T set(int arg0, T arg1) {
		return m_innerList.set(arg0, arg1);
	}

	public int size() {
		return m_innerList.size();
	}

	public void sort(Comparator<? super T> arg0) {
		m_innerList.sort(arg0);
	}

	public Spliterator<T> spliterator() {
		return m_innerList.spliterator();
	}

	public Stream<T> stream() {
		return m_innerList.stream();
	}

	public List<T> subList(int arg0, int arg1) {
		return m_innerList.subList(arg0, arg1);
	}

	public Object[] toArray() {
		return m_innerList.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] arg0) {
		return m_innerList.toArray(arg0);
	}

	public Trace<T> suffixFrom(int index)
	{
		Trace<T> out = new Trace<T>();
		List<T> out_list = subList(index, size());
		out.addAll(out_list);
		return out;
	}
	
	@Override
	public int hashCode()
	{
		return size();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Trace<?>))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		Trace<T> t = (Trace<T>) o;
		return equals(t);
	}
	
	/**
	 * Checks if two traces are equal. This is the case when
	 * all their events are pairwise equal.
	 * @param t The other trace
	 * @return true if traces are equal, false otherwise
	 */
	private boolean equals(Trace<T> t)
	{
		if (t.size() != size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			T e1 = get(i);
			T e2 = t.get(i);
			if (!e1.equals(e2))
			{
				return false;
			}
		}
		return true;
	}
	
}
