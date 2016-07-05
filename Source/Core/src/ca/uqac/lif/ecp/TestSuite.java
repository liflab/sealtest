package ca.uqac.lif.ecp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TestSuite<T extends Event> implements Set<Trace<T>>
{
	/**
	 * The internal set used to contain the elements
	 */
	protected Set<Trace<T>> m_set;
	
	/**
	 * Creates a new empty test suite
	 */
	public TestSuite()
	{
		super();
		m_set = new HashSet<Trace<T>>();
	}
	
	/**
	 * Gets the total length of the test suite
	 * @return The total length
	 */
	public int getTotalLength()
	{
		int length = 0;
		for (Trace<T> trace : this)
		{
			length += trace.size();
		}
		return length;
	}

	public boolean add(Trace<T> e) {
		return m_set.add(e);
	}

	public boolean addAll(Collection<? extends Trace<T>> c) {
		return m_set.addAll(c);
	}

	public void clear() {
		m_set.clear();
	}

	public boolean contains(Object o) {
		return m_set.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return m_set.containsAll(c);
	}

	public boolean equals(Object o) {
		return m_set.equals(o);
	}

	public void forEach(Consumer<? super Trace<T>> arg0) {
		m_set.forEach(arg0);
	}

	public int hashCode() {
		return m_set.hashCode();
	}

	public boolean isEmpty() {
		return m_set.isEmpty();
	}

	public Iterator<Trace<T>> iterator() {
		return m_set.iterator();
	}

	public Stream<Trace<T>> parallelStream() {
		return m_set.parallelStream();
	}

	public boolean remove(Object o) {
		return m_set.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return m_set.removeAll(c);
	}

	public boolean removeIf(Predicate<? super Trace<T>> filter) {
		return m_set.removeIf(filter);
	}

	public boolean retainAll(Collection<?> c) {
		return m_set.retainAll(c);
	}

	public int size() {
		return m_set.size();
	}

	public Spliterator<Trace<T>> spliterator() {
		return m_set.spliterator();
	}

	public Stream<Trace<T>> stream() {
		return m_set.stream();
	}

	public Object[] toArray() {
		return m_set.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return m_set.toArray(a);
	}
}
