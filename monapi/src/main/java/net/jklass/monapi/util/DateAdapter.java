package net.jklass.monapi.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

public class DateAdapter extends XmlAdapter<String, DateTime>
{

	public String marshal(DateTime v) throws Exception
	{
		String retString = "";
		retString = v.year().get() + "-" + this.padInt(v.monthOfYear().get())
				+ "-" + this.padInt(v.dayOfMonth().get()) + " "
				+ this.padInt(v.hourOfDay().get()) + ":"
				+ this.padInt(v.minuteOfHour().get()) + ":"
				+ this.padInt(v.secondOfMinute().get());
		return retString;
	}

	private String padInt(int num)
	{
		return String.format("%02d", num);
	}

	public DateTime unmarshal(String v) throws Exception
	{
		return new DateTime(v);
	}
}
