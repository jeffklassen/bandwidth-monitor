package net.jklass.monapi.web;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.jklass.monapi.dao.BandwidthDao;
import net.jklass.monapi.service.BandwidthSumsImpl;
import net.jklass.monapi.service.BandwidthSums;
import net.jklass.monapi.viewmodel.BandwidthListViewModel;
import net.jklass.monapi.viewmodel.DetailViewModel;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MonWebImpl implements MonWeb {

	static final Logger logger = Logger.getLogger(MonWebImpl.class);
	static final boolean debug = logger.isDebugEnabled();

	protected BandwidthDao bandwidthDao;

	public BandwidthDao getBandwidthDao() {
		return bandwidthDao;
	}

	@Override
	public Response getDays(int num) {
		BandwidthListViewModel model = new BandwidthListViewModel();

		BandwidthSums service = new BandwidthSumsImpl(bandwidthDao);
		model.setBandwidthList(service.getByDay(num));

		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	@Override
	public Response getDetails(String startDateTimeStr, String endDateTimeStr) {

		// convert input strings to datetime objects
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime startDateTime = formatter.parseDateTime(startDateTimeStr);
		DateTime endDateTime = formatter.parseDateTime(endDateTimeStr);

		logger.debug(startDateTimeStr + " -> " + startDateTime);
		logger.debug(endDateTimeStr + " -> " + endDateTime);

		// instantiate model and service objects
		DetailViewModel model = this.bandwidthDao.getDetails(startDateTime,
				endDateTime);

		// pass this off to the response
		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	@Override
	public Response getHours(int num) {
		BandwidthListViewModel model = new BandwidthListViewModel();

		BandwidthSums service = new BandwidthSumsImpl(bandwidthDao);
		model.setBandwidthList(service.getByHour(num));

		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	@Override
	public Response getMinutes(int num) {
		BandwidthListViewModel model = new BandwidthListViewModel();

		BandwidthSums service = new BandwidthSumsImpl(bandwidthDao);
		model.setBandwidthList(service.getByMinute(num));

		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	@Override
	public Response getSeconds(int num) {
		BandwidthListViewModel model = new BandwidthListViewModel();
		
		BandwidthSums service = new BandwidthSumsImpl(bandwidthDao);
		model.setBandwidthList(service.getBySecond(num));

		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	@Override
	public Response getTotal() {
		BandwidthListViewModel model = new BandwidthListViewModel();

		BandwidthSums service = new BandwidthSumsImpl(bandwidthDao);
		model.setSingleBandwidth(service.getTotal());

		ResponseBuilder r = Response.ok(model);
		return r.build();
	}

	public void setBandwidthDao(BandwidthDao bandwidthDao) {
		this.bandwidthDao = bandwidthDao;
	}

}
