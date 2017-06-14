package com.zbiti.common.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataVO<K, V>
{
	Map<K, V> data = new HashMap<K, V>();

	public Map<K, V> getData()
	{
		return data;
	}

	public void setData(Map<K, V> data)
	{
		this.data = data;
	}

	// @Override
	public int size()
	{

		return data.size();
	}

	// @Override
	public boolean isEmpty()
	{

		return data.isEmpty();
	}

	// @Override
	public boolean containsKey(Object key)
	{

		return data.containsKey(key);
	}

	// @Override
	public boolean containsValue(Object value)
	{

		return data.containsValue(value);
	}

	// @Override
	public V get(Object key)
	{
		// TODO Auto-generated method stub
		return data.get(key);
	}

	// @Override
	public V put(K key, V value)
	{

		return data.put(key, value);
	}

	// @Override
	public V remove(Object key)
	{
		return data.remove(key);
	}

	// @Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		data.putAll(m);
	}

	// @Override
	public void clear()
	{
		data.clear();

	}

	// @Override
	public Set<K> keySet()
	{
		return data.keySet();
	}

	// @Override
	public Collection<V> values()
	{
		return data.values();
	}

	// @Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return data.entrySet();
	}
}
