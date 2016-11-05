package ca.uqac.lif.ecp.graphs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Labelling of a Graph. This is a mapping from a vertex ID (integer)
 * to objects of type <code>V</code>.
 * @author Sylvain Hallé
 *
 * @param <V> The type of the mapping
 */
public class VertexLabelling<V> implements Map<Integer,V>
{
	/**
	 * The internal map storing the labelling
	 */
	private Map<Integer,V> m_map;
	
	/**
	 * Creates a new empty vertex labelling
	 */
	public VertexLabelling()
	{
		super();
		m_map = new HashMap<Integer,V>();
	}
	
	public void clear() {
		m_map.clear();
	}

	public V compute(Integer arg0,
			BiFunction<? super Integer, ? super V, ? extends V> arg1) {
		return m_map.compute(arg0, arg1);
	}

	public V computeIfAbsent(Integer arg0,
			Function<? super Integer, ? extends V> arg1) {
		return m_map.computeIfAbsent(arg0, arg1);
	}

	public V computeIfPresent(Integer arg0,
			BiFunction<? super Integer, ? super V, ? extends V> arg1) {
		return m_map.computeIfPresent(arg0, arg1);
	}

	public boolean containsKey(Object arg0) {
		return m_map.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return m_map.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<Integer, V>> entrySet() {
		return m_map.entrySet();
	}

	public boolean equals(Object arg0) {
		return m_map.equals(arg0);
	}

	public void forEach(BiConsumer<? super Integer, ? super V> arg0) {
		m_map.forEach(arg0);
	}

	public V get(Object arg0) {
		return m_map.get(arg0);
	}

	public V getOrDefault(Object arg0, V arg1) {
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

	public V merge(Integer arg0, V arg1,
			BiFunction<? super V, ? super V, ? extends V> arg2) {
		return m_map.merge(arg0, arg1, arg2);
	}

	public V put(int arg0, V arg1) {
		return m_map.put(arg0, arg1);
	}
	
	public V put(Integer arg0, V arg1) {
		return m_map.put(arg0, arg1);
	}

	public void putAll(Map<? extends Integer, ? extends V> arg0) {
		m_map.putAll(arg0);
	}

	public V putIfAbsent(Integer arg0, V arg1) {
		return m_map.putIfAbsent(arg0, arg1);
	}

	public boolean remove(Object arg0, Object arg1) {
		return m_map.remove(arg0, arg1);
	}

	public V remove(Object arg0) {
		return m_map.remove(arg0);
	}

	public boolean replace(Integer arg0, V arg1, V arg2) {
		return m_map.replace(arg0, arg1, arg2);
	}

	public V replace(Integer arg0, V arg1) {
		return m_map.replace(arg0, arg1);
	}

	public void replaceAll(
			BiFunction<? super Integer, ? super V, ? extends V> arg0) {
		m_map.replaceAll(arg0);
	}

	public int size() {
		return m_map.size();
	}

	public Collection<V> values() {
		return m_map.values();
	}

}
