package net.jklass.monapi.dao.elasticsearch.hitmapper;

import java.util.Map;

public interface HitMapper<T>
{
	public T mapHit(Map<String, Object> hit);
}
