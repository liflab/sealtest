/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Alphabet<T> implements Set<T> 
{
	/**
	 * The inner set containing the elements
	 */
	private Set<T> m_elements;
	
	/**
	 * Creates an empty alphabet
	 */
	public Alphabet()
	{
		super();
		m_elements = new HashSet<T>();
	}

	public boolean add(T arg0) {
		return m_elements.add(arg0);
	}

	public boolean addAll(Collection<? extends T> arg0) {
		return m_elements.addAll(arg0);
	}

	public void clear() {
		m_elements.clear();
	}

	public boolean contains(Object arg0) {
		return m_elements.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		return m_elements.containsAll(arg0);
	}

	public boolean equals(Object arg0) {
		return m_elements.equals(arg0);
	}

	public void forEach(Consumer<? super T> arg0) {
		m_elements.forEach(arg0);
	}

	public int hashCode() {
		return m_elements.hashCode();
	}

	public boolean isEmpty() {
		return m_elements.isEmpty();
	}

	public Iterator<T> iterator() {
		return m_elements.iterator();
	}

	public Stream<T> parallelStream() {
		return m_elements.parallelStream();
	}

	public boolean remove(Object arg0) {
		return m_elements.remove(arg0);
	}

	public boolean removeAll(Collection<?> arg0) {
		return m_elements.removeAll(arg0);
	}

	public boolean removeIf(Predicate<? super T> arg0) {
		return m_elements.removeIf(arg0);
	}

	public boolean retainAll(Collection<?> arg0) {
		return m_elements.retainAll(arg0);
	}

	public int size() {
		return m_elements.size();
	}

	public Spliterator<T> spliterator() {
		return m_elements.spliterator();
	}

	public Stream<T> stream() {
		return m_elements.stream();
	}

	public Object[] toArray() {
		return m_elements.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] arg0) {
		return m_elements.toArray(arg0);
	}
	
	@Override
	public String toString()
	{
		return m_elements.toString();
	}
	
}
