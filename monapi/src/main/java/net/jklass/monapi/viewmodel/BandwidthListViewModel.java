package net.jklass.monapi.viewmodel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.jklass.monapi.dao.data.BandwidthImpl;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BandwidthListViewModel
{
	@XmlElement
	private List<BandwidthImpl> bandwidthList;

	@XmlElement
	private BandwidthImpl singleBandwidth;

	public List<BandwidthImpl> getBandwidthList()
	{
		return bandwidthList;
	}

	public BandwidthImpl getSingleBandwidth()
	{
		return singleBandwidth;
	}

	public void setBandwidthList(List<BandwidthImpl> list)
	{
		this.bandwidthList = list;
	}

	public void setSingleBandwidth(BandwidthImpl singleBandwidth)
	{
		this.singleBandwidth = singleBandwidth;

	}
}
