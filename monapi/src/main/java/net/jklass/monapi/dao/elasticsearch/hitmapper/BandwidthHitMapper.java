package net.jklass.monapi.dao.elasticsearch.hitmapper;

import java.util.Map;

import net.jklass.monapi.dao.data.BandwidthImpl;

import org.joda.time.DateTime;

public class BandwidthHitMapper implements HitMapper<BandwidthImpl>
{
	private double convertToKB(Number byteCount)
	{
		return byteCount.doubleValue() / (double) 1024;
	}

	@Override
	public BandwidthImpl mapHit(Map<String, Object> hit)
	{
		System.out.println(hit);
		BandwidthImpl bandwidth = new BandwidthImpl();
		bandwidth.setTimestamp(new DateTime((String) hit.get("dateTime")));
		try
		{
			bandwidth.setDown(convertToKB((Number) hit.get("down")));
		}
		catch (NullPointerException e)
		{
			bandwidth.setDown(0);
		}
		try
		{
			bandwidth.setUp(convertToKB((Number) hit.get("up")));
		}
		catch (NullPointerException e)
		{
			bandwidth.setUp(0);
		}
		return bandwidth;
	}
}