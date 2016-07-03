package ca.uqac.lif.ecp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class VertexLabelling<U extends Object> implements Map<Integer,U>
{
	/**
	 * The internal map storing the labelling
	 */
	private Map<Integer,U> m_map;
	
	/**
	 * Creates a new empty vertex labelling
	 */
	public VertexLabelling()
	{
		super();
		m_map = new HashMap<Integer,U>();
	}

	public void clear() {
		m_map.clear();
	}

	public U compute(Integer arg0,
			BiFunction<? super Integer, ? super U, ? extends U> arg1) {
		return m_map.compute(arg0, arg1);
	}

	public U computeIfAbsent(Integer arg0,
			Function<? super Integer, ? extends U> arg1) {
		return m_map.computeIfAbsent(arg0, arg1);
	}

	public U computeIfPresent(Integer arg0,
			BiFunction<? super Integer, ? super U, ? extends U> arg1) {
		return m_map.computeIfPresent(arg0, arg1);
	}

	public boolean containsKey(Object arg0) {
		return m_map.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return m_map.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<Integer, U>> entrySet() {
		return m_map.entrySet();
	}

	public boolean equals(Object arg0) {
		return m_map.equals(arg0);
	}

	public void forEach(BiConsumer<? super Integer, ? super U> arg0) {
		m_map.forEach(arg0);
	}

	public U get(Object arg0) {
		return m_map.get(arg0);
	}

	public U getOrDefault(Object arg0, U arg1) {
		return m_map.getOrDefault(arg0, arg1);
	}

	public int hashCode() {
		return m_map.hashCode();
	}

	public boolean isEmpty() {
		return m_map.isEmpty();
	}

	public Set<Integer> keySet() {
		return m_map.keySet();
	}

	public U merge(Integer arg0, U arg1,
			BiFunction<? super U, ? super U, ? extends U> arg2) {
		return m_map.merge(arg0, arg1, arg2);
	}

	public U put(Integer arg0, U arg1) {
		return m_map.put(arg0, arg1);
	}

	public void putAll(Map<? extends Integer, ? extends U> arg0) {
		m_map.putAll(arg0);
	}

	public U putIfAbsent(Integer arg0, U arg1) {
		return m_map.putIfAbsent(arg0, arg1);
	}

	public boolean remove(Object arg0, Object arg1) {
		return m_map.remove(arg0, arg1);
	}

	public U remove(Object arg0) {
		return m_map.remove(arg0);
	}

	public boolean replace(Integer arg0, U arg1, U arg2) {
		return m_map.replace(arg0, arg1, arg2);
	}

	public U replace(Integer arg0, U arg1) {
		return m_map.replace(arg0, arg1);
	}

	public void replaceAll(
			BiFunction<? super Integer, ? super U, ? extends U> arg0) {
		m_map.replaceAll(arg0);
	}

	public int size() {
		return m_map.size();
	}

	public Collection<U> values() {
		return m_map.values();
	}
}
