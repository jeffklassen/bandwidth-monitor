package net.jklass.monapi.viewmodel;

import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class DetailEntry<K, V> implements Entry<K, V>
{
	@XmlAttribute(name = "key", required = true)
	private K key;
	@XmlAttribute(name = "value", required = true)
	private V value;

	public DetailEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey()
	{
		return key;
	}

	@Override
	public V getValue()
	{
		// TODO Auto-generated method stub
		return value;
	}

	public void setKey(K key)
	{
		this.key = key;
	}

	@Override
	public V setValue(V value)
	{
		this.value = value;
		return null;
	}
}
