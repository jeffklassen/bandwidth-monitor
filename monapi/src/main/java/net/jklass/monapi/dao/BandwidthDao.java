package net.jklass.monapi.dao;

import java.util.List;

import net.jklass.monapi.dao.data.BandwidthImpl;
import net.jklass.monapi.dao.data.Time;
import net.jklass.monapi.viewmodel.DetailViewModel;

import org.joda.time.DateTime;

public interface BandwidthDao {

	List<BandwidthImpl> getLastEntries(Time table);

	List<BandwidthImpl> getLastEntries(Time table, int numEntries);

	String getMostRecentHostname(String ip, DateTime dateTimeString);

	DetailViewModel getDetails(DateTime startDateTime, DateTime endDateTime);

	BandwidthImpl getTotal();

}