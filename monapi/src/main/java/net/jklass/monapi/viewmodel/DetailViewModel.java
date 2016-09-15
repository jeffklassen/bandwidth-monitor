package net.jklass.monapi.viewmodel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailViewModel
{
	@XmlElement
	private double totalUp;
	@XmlElement
	private double totalDown;

	@XmlElement
	private List<DetailEntry<String, Double>> downloadByInternalUrl;
	@XmlElement
	private List<DetailEntry<String, Double>> downloadByExternalUrl;

	@XmlElement
	private List<DetailEntry<String, Double>> uploadByInternalUrl;
	@XmlElement
	private List<DetailEntry<String, Double>> uploadByExternalUrl;

	public List<DetailEntry<String, Double>> getDownloadByExternalUrl()
	{
		return downloadByExternalUrl;
	}

	public List<DetailEntry<String, Double>> getDownloadByInternalUrl()
	{
		return downloadByInternalUrl;
	}

	public double getTotalDown()
	{
		return totalDown;
	}

	public double getTotalUp()
	{
		return totalUp;
	}

	public List<DetailEntry<String, Double>> getUploadByExternalUrl()
	{
		return uploadByExternalUrl;
	}

	public List<DetailEntry<String, Double>> getUploadByInternalUrl()
	{
		return uploadByInternalUrl;
	}

	public void setDownloadByExternalUrl(List<DetailEntry<String, Double>> list)
	{
		this.downloadByExternalUrl = list;
	}

	public void setDownloadByInternalUrl(List<DetailEntry<String, Double>> list)
	{
		this.downloadByInternalUrl = list;
	}

	public void setTotalDown(double totalDown)
	{
		this.totalDown = totalDown;
	}

	public void setTotalUp(double totalUp)
	{
		this.totalUp = totalUp;
	}

	public void setUploadByExternalUrl(List<DetailEntry<String, Double>> list)
	{
		this.uploadByExternalUrl = list;
	}

	public void setUploadByInternalUrl(List<DetailEntry<String, Double>> list)
	{
		this.uploadByInternalUrl = list;
	}
}
