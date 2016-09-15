package net.jklass.monapi.dao.elasticsearch.hitmapper;

import org.elasticsearch.search.aggregations.Aggregation;


public interface AggMapper<T>
{

	public T mapAgg(Aggregation agg);

}
