package net.jklass.monapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jklass.monapi.dao.BandwidthDao;
import net.jklass.monapi.dao.data.Bandwidth;
import net.jklass.monapi.dao.data.BandwidthImpl;
import net.jklass.monapi.dao.data.Time;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class BandwidthSumsImpl implements BandwidthSums
{

	static final Logger logger = Logger.getLogger(BandwidthSumsImpl.class);

	static final boolean debug = logger.isDebugEnabled();

	private BandwidthDao bandwidthDao;

	public BandwidthSumsImpl(BandwidthDao bandwidthDao)
	{
		this.bandwidthDao = bandwidthDao;
	}

	private List<BandwidthImpl> addZeroTimes(int numTimeIncrements,
			List<BandwidthImpl> bandwidthsFromDB, Time time)
	{
		List<BandwidthImpl> normalizedBW = new ArrayList<BandwidthImpl>();

		DateTime mostRecentDateTime = bandwidthsFromDB.get(0).getTimestamp();

		// figure out the most recent time returned from the DB
		logger.debug("Started searching for max time at " + DateTime.now());
		for (BandwidthImpl bw : bandwidthsFromDB)
		{

			if (bw.getTimestamp().getMillis() > mostRecentDateTime.getMillis())
			{
				mostRecentDateTime = bw.getTimestamp();
			}

		}
		logger.debug("Stopped searching for max time at " + DateTime.now());
		logger.debug("max time stamp is: " + mostRecentDateTime);

		logger.debug("Started normalizing bw entries at " + DateTime.now());
		// we need to make sure that there are no missing seconds from the
		// database
		for (int i = 0; i < numTimeIncrements; i++)
		{

			DateTime workingTime = null;

			if (time == Time.SECOND)
			{
				workingTime = mostRecentDateTime.minusSeconds(i);
			}
			else if (time == Time.HOUR)
			{
				workingTime = mostRecentDateTime.minusHours(i);
			}
			long workingTimeMillis = workingTime.getMillis();
			BandwidthImpl newBW = null;
			for (BandwidthImpl bw : bandwidthsFromDB)
			{

				if (bw.getTimestamp().getMillis() == workingTimeMillis)
				{
					newBW = bw;
					break;
				}
			}
			if (newBW != null)
			{
				// remove the found object so we dont have to iterate through it
				// again
				bandwidthsFromDB.remove(newBW);
				normalizedBW.add(newBW);
			}
			else
			{
				normalizedBW.add(BandwidthImpl.getZeroBandwidth(workingTime));
			}

		}
		logger.debug("Stopped normalizing bw entries at " + DateTime.now());
		return normalizedBW;
	}

	/* (non-Javadoc)
	 * @see net.jklass.monapi.service.BandwidthSums#getByDay(int)
	 */
	@Override
	public List<BandwidthImpl> getByDay(int numDays)
	{
		List<BandwidthImpl> bandwidths = this.getByHour(numDays * 24);

		return this.groupBandwidths(bandwidths, Time.DAY);
	}

	/* (non-Javadoc)
	 * @see net.jklass.monapi.service.BandwidthSums#getByHour(int)
	 */
	@Override
	public List<BandwidthImpl> getByHour(int numHours)
	{
		List<BandwidthImpl> bandwidthsFromDB = bandwidthDao.getLastEntries(
				Time.HOUR, numHours);

		List<BandwidthImpl> normalizedBW = addZeroTimes(numHours,
				bandwidthsFromDB, Time.HOUR);

		return normalizedBW;
	}

	/* (non-Javadoc)
	 * @see net.jklass.monapi.service.BandwidthSums#getByMinute(int)
	 */
	@Override
	public List<BandwidthImpl> getByMinute(int numMinutes)
	{

		List<BandwidthImpl> bandwidths = this.getBySecond(numMinutes * 60);

		logger.debug(bandwidths.size());

		return this.groupBandwidths(bandwidths, Time.MINUTE);
	}

	/* (non-Javadoc)
	 * @see net.jklass.monapi.service.BandwidthSums#getBySecond(int)
	 */
	@Override
	public List<BandwidthImpl> getBySecond(int numSeconds)
	{

		List<BandwidthImpl> bandwidthsFromDB = bandwidthDao.getLastEntries(
				Time.SECOND, numSeconds);

		List<BandwidthImpl> normalizedBW = addZeroTimes(numSeconds,
				bandwidthsFromDB, Time.SECOND);

		return normalizedBW;
	}

	/* (non-Javadoc)
	 * @see net.jklass.monapi.service.BandwidthSums#getTotal()
	 */
	@Override
	public BandwidthImpl getTotal()
	{
		BandwidthImpl totalBandwidth = bandwidthDao.getTotal();

		return totalBandwidth;
	}

	/**
	 * Groups bandwidths into @Time increments
	 * 
	 * @param bandwidths
	 * @param toTime
	 *            - how to group bandwidths
	 * @return List of bandwidths grouped by Time type
	 * 
	 */
	private List<BandwidthImpl> groupBandwidths(List<BandwidthImpl> bandwidths,
			Time toTime)
	{
		List<BandwidthImpl> normalizedBandwidths = new ArrayList<BandwidthImpl>();

		Map<Long, BandwidthImpl> bandwidthMap = new HashMap<Long, BandwidthImpl>();

		// create bandwidth map object with up = 0, down = 0
		for (Bandwidth workingBandwidth : bandwidths)
		{
			DateTime d = new DateTime(workingBandwidth.getTimestamp());
			DateTime workingDateTime;
			// this is time at the start of the workingTime
			if (toTime == Time.MINUTE)
			{
				workingDateTime = new DateTime(d.year().get(), d.monthOfYear()
						.get(), d.dayOfMonth().get(), d.hourOfDay().get(), d
						.minuteOfHour().get());
			}
			else
			{
				workingDateTime = new DateTime(d.year().get(), d.monthOfYear()
						.get(), d.dayOfMonth().get(), 0, 0);
			}

			if (bandwidthMap.containsKey(workingDateTime.getMillis()))
			{
				// get the object in the map for the given minute and increment
				// both up and down
				Bandwidth b = bandwidthMap.get(workingDateTime.getMillis());
				b.setDown(b.getDown() + workingBandwidth.getDown());
				b.setUp(b.getUp() + workingBandwidth.getUp());
				
				logger.debug("using real bw for time: " + workingDateTime);

			}
			else
			{
				bandwidthMap.put(workingDateTime.getMillis(),
						BandwidthImpl.getZeroBandwidth(workingDateTime));
				logger.debug("putting zero bw for time: " + workingDateTime);
			}

		}

		for (BandwidthImpl b : bandwidthMap.values())
		{
			normalizedBandwidths.add(b);
		}

		logger.debug(bandwidthMap.size());
		return normalizedBandwidths;
	}

}
