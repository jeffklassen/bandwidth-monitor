package net.jklass.monapi.dao.elasticsearch.hitmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

import net.jklass.monapi.viewmodel.DetailEntry;
import net.jklass.monapi.viewmodel.DetailViewModel;

public class DetailAggMapper implements AggMapper<DetailViewModel> {
	static final Logger logger = Logger.getLogger(DetailAggMapper.class);
	static final boolean debug = logger.isDebugEnabled();

	@Override
	public DetailViewModel mapAgg(Aggregation agg) {
		DetailViewModel vm = new DetailViewModel();
		Map<String, DetailEntry<String, Double>> map = new HashMap<String, DetailEntry<String, Double>>();
		List<String> groupingNames = new ArrayList<String>();
		groupingNames.add("source");
		groupingNames.add("dest");

		// Filters agg = query().getAggregations().get("agg");

		for (Filters.Bucket filterAgg : ((Filters) agg).getBuckets()) {
			String directionString = (String) filterAgg.getKey();
			for (String groupingName : groupingNames) {
				Terms groupingAgg = filterAgg.getAggregations()
						.get(groupingName);
				List<DetailEntry<String, Double>> detailEntries = new ArrayList<DetailEntry<String, Double>>();

				for (Terms.Bucket groupingAggBucket : groupingAgg
						.getBuckets()) {
					Sum sums = groupingAggBucket.getAggregations()
							.get("sumagg");

					String subkey = (String) groupingAggBucket.getKey(); // bucket
																			// key
					logger.debug(filterAgg.getKey() + groupingName + "key [ "
							+ subkey + "], size [" + sums.getValue() + "]");
					detailEntries.add(new DetailEntry<String, Double>(subkey,
							sums.getValue() / 1024));
				}

				if (directionString.equals("download")) {
					if (groupingName.equals("dest")) {
						vm.setDownloadByInternalUrl(detailEntries);
					} else if (groupingName.equals("source")) {
						vm.setDownloadByExternalUrl(detailEntries);

					}
				} else if (directionString.equals("upload")) {
					if (groupingName.equals("dest")) {
						vm.setUploadByExternalUrl(detailEntries);
					} else if (groupingName.equals("source")) {
						vm.setUploadByInternalUrl(detailEntries);

					}
				}

			}

			Sum sums = filterAgg.getAggregations().get("sumagg");

			if (directionString.equals("download")) {
				vm.setTotalDown(sums.getValue() / 1024);
			} else if (directionString.equals("upload")) {
				vm.setTotalUp(sums.getValue() / 1024);
			}

			logger.debug(filterAgg.getKey() + "TOTAL size [" + sums.getValue()
					+ "]");
		}
		return vm;
	}

}
