package net.jklass.monapi.dao.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.jklass.monapi.util.DateAdapter;

import org.joda.time.DateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class BandwidthImpl implements Bandwidth
{
	public static BandwidthImpl getZeroBandwidth(DateTime timestamp)
	{
		BandwidthImpl bandwidth = new BandwidthImpl();
		bandwidth.setTimestamp(timestamp);
		bandwidth.setUp(0);
		bandwidth.setDown(0);
		return bandwidth;
	}

	@XmlElement(name = "timestamp", required = true)
	@XmlJavaTypeAdapter(value = DateAdapter.class)
	private DateTime timestamp;

	@XmlElement
	private double up;

	@XmlElement
	private double down;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#getDown()
	 */
	@Override
	public double getDown()
	{
		return down;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#getDate()
	 */
	@Override
	public DateTime getTimestamp()
	{
		return timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#getUp()
	 */
	@Override
	public double getUp()
	{
		return up;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#setDown(double)
	 */
	@Override
	public void setDown(double down)
	{
		this.down = down;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#setDate(java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(DateTime dateTime)
	{
		this.timestamp = dateTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jklass.monapi.dao.data.Bandwidth#setUp(double)
	 */
	@Override
	public void setUp(double up)
	{
		this.up = up;
	}
}
