package net.jklass.monapi.dao.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import net.jklass.monapi.dao.elasticsearch.hitmapper.AggMapper;
import net.jklass.monapi.dao.elasticsearch.hitmapper.HitMapper;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

public abstract class ESExecutor {
	static final Logger logger = Logger.getLogger(ESExecutor.class);
	static final boolean debug = logger.isDebugEnabled();

	protected Client client;

	public ESExecutor(Client client) {
		this.client = client;
	}

	protected abstract SearchResponse getSearchResponse();

	public <T> T mapAgg(AggMapper<T> mapper, String aggName) {

		T returnVar = mapper.mapAgg(query().getAggregations().get(aggName));

		return returnVar;
	}

	public <T> List<T> map(HitMapper<T> mapper) {

		List<T> list = new ArrayList<T>();
		for (SearchHit hit : query().getHits()) {
			list.add(mapper.mapHit(hit.getSource()));
		}
		return list;
	}

	public <T> List<T> mapScroll(HitMapper<T> mapper) {
		List<T> list = new ArrayList<T>();
		SearchResponse scrollResp = query();
		while (scrollResp.getHits().getHits().length > 0) {

			for (SearchHit hit : scrollResp.getHits().getHits()) {

				list.add(mapper.mapHit(hit.getSource()));

			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
					.setScroll(new TimeValue(600000)).execute().actionGet();
			logger.debug("Scrolling list has size: " + list.size());
		}

		return list;
	}

	public SearchResponse query() {
		SearchResponse response;

		response = this.getSearchResponse();

		// client.close();

		return response;
	}

}