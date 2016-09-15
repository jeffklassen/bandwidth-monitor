package net.jklass.monapi.service;

import java.util.List;

import net.jklass.monapi.dao.data.BandwidthImpl;

public interface BandwidthSums {

	public abstract List<BandwidthImpl> getByDay(int numDays);

	public abstract List<BandwidthImpl> getByHour(int numHours);

	public abstract List<BandwidthImpl> getByMinute(int numMinutes);

	public abstract List<BandwidthImpl> getBySecond(int numSeconds);

	public abstract BandwidthImpl getTotal();

}