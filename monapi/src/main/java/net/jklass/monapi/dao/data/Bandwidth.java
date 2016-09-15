package net.jklass.monapi.dao.data;

import org.joda.time.DateTime;

public interface Bandwidth
{

	public abstract double getDown();

	public abstract DateTime getTimestamp();

	public abstract double getUp();

	public abstract void setDown(double down);

	public abstract void setTimestamp(DateTime date);

	public abstract void setUp(double up);

}