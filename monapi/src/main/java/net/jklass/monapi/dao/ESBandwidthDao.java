package net.jklass.monapi.dao;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.List;

import net.jklass.monapi.dao.data.BandwidthImpl;
import net.jklass.monapi.dao.data.Time;
import net.jklass.monapi.dao.elasticsearch.ESExecutor;
import net.jklass.monapi.dao.elasticsearch.hitmapper.BandwidthHitMapper;
import net.jklass.monapi.dao.elasticsearch.hitmapper.DetailAggMapper;
import net.jklass.monapi.viewmodel.DetailViewModel;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.index.query.MultiTermQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.elasticsearch.action.search.SearchRequestBuilder;

public class ESBandwidthDao implements BandwidthDao {

	static final Logger logger = Logger.getLogger(ESBandwidthDao.class);
	static final boolean debug = logger.isDebugEnabled();
	private static final String LOCALSUBNET = "192.168.1";
	private static final String HOUR = "hour";
	private static final String SECOND = "second";
	private Client client;

	public ESBandwidthDao() {
		this.client = getClient();
	}

	private Client getClient() {
		if (this.client == null ) {
			Client client = null;
			try {
				client = TransportClient
						.builder()
						.settings(
								Settings.builder()
										.put("cluster.name", "jeff-es").build())
						.build()
						.addTransportAddress(
								new InetSocketTransportAddress(InetAddress
										.getByName("192.168.1.41"), 9300))
						.addTransportAddress(
								new InetSocketTransportAddress(InetAddress
										.getByName("192.168.1.42"), 9200))
						.addTransportAddress(
								new InetSocketTransportAddress(InetAddress
										.getByName("192.168.1.43"), 9300))
						.addTransportAddress(
								new InetSocketTransportAddress(InetAddress
										.getByName("192.168.1.44"), 9300))
						.addTransportAddress(
								new InetSocketTransportAddress(InetAddress
										.getByName("192.168.1.45"), 9300));
				this.client = client;
			} catch (UnknownHostException e) {
				logger.error(e);
			}
		}
		return this.client;
	}

	@Override
	public List<BandwidthImpl> getLastEntries(Time table) {
		return this.getLastEntries(table, 0);
	}

	@Override
	public List<BandwidthImpl> getLastEntries(final Time table,
			final int numEntries) {
		logger.debug("table: " + getTableName(table));

		ESExecutor executor = new ESExecutor(getClient()) {
			protected SearchResponse getSearchResponse() {
				SearchRequestBuilder searchRequestBuilder = client
						.prepareSearch("bmon")
						.setTypes(getTableName(table))
						.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						.addSort(
								SortBuilders.fieldSort("dateTime").order(
										SortOrder.DESC)).setFrom(0)
						.setSize(numEntries).setExplain(true);

				logger.debug(searchRequestBuilder);

				return searchRequestBuilder.execute().actionGet();

			}
		};

		return executor.map(new BandwidthHitMapper());
	}

	@Override
	public String getMostRecentHostname(String ip, DateTime dateTimeString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DetailViewModel getDetails(final DateTime startDateTime,
			final DateTime endDateTime) {
		ESExecutor executor = new ESExecutor(getClient()) {
			protected SearchResponse getSearchResponse() {
				MultiTermQueryBuilder downDirectonFilter = QueryBuilders
						.prefixQuery("destAddr", LOCALSUBNET);

				MultiTermQueryBuilder upDirectonFilter = QueryBuilders
						.prefixQuery("sourceAddr", LOCALSUBNET);

				MultiTermQueryBuilder dateDirectonFilter = QueryBuilders
						.rangeQuery("dateTime").lte(endDateTime)
						.gte(startDateTime);

				SumBuilder sumaggregation = AggregationBuilders.sum("sumagg")
						.field("size");

				SearchRequestBuilder requestBuilder = client.prepareSearch(
						"bmon-detail").setTypes("packet");

				requestBuilder.addAggregation(AggregationBuilders
						.filters("agg")
						.filter("download",
								QueryBuilders.boolQuery().filter(
										QueryBuilders.boolQuery()
												.must(dateDirectonFilter)
												.must(downDirectonFilter)))

						.filter("upload",
								QueryBuilders.boolQuery().filter(
										QueryBuilders.boolQuery()
												.must(dateDirectonFilter)
												.must(upDirectonFilter)))
						.subAggregation(sumaggregation)
						.subAggregation(
								AggregationBuilders
										.terms("source")
										.field("sourceAddr")
										.size(0)
										.order(Terms.Order.aggregation(
												"sumagg", false))
										.subAggregation(sumaggregation))
						.subAggregation(
								AggregationBuilders
										.terms("dest")
										.field("destAddr")
										.size(0)
										.order(Terms.Order.aggregation(
												"sumagg", false))
										.subAggregation(sumaggregation)));
				logger.debug(requestBuilder);
				return requestBuilder.execute().actionGet();
			}
		};
		return executor.mapAgg(new DetailAggMapper(), "agg");
	}

	private String getTableName(Time table) {
		if (table == Time.HOUR) {
			return HOUR;
		} else {
			return SECOND;
		}
	}

	@Override
	public BandwidthImpl getTotal() {
		// TODO Auto-generated method stub
		return null;
	}

}
