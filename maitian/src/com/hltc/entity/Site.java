package com.hltc.entity;

/**
 * Site entity. @author MyEclipse Persistence Tools
 */

public class Site implements java.io.Serializable {

	// Fields

	private String siteId;
	private Double lon;
	private Double lat;
	private String name;
	private String address;
	private String telphone;
	private String source;
	private String gtype;
	private String mtype;

	// Constructors

	/** default constructor */
	public Site() {
	}

	/** minimal constructor */
	public Site(String siteId) {
		this.siteId = siteId;
	}

	/** full constructor */
	public Site(String siteId, Double lon, Double lat, String name,
			String address, String telphone, String source, String gtype,
			String mtype) {
		this.siteId = siteId;
		this.lon = lon;
		this.lat = lat;
		this.name = name;
		this.address = address;
		this.telphone = telphone;
		this.source = source;
		this.gtype = gtype;
		this.mtype = mtype;
	}

	// Property accessors

	public String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Double getLon() {
		return this.lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return this.lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelphone() {
		return this.telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getGtype() {
		return this.gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public String getMtype() {
		return this.mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

}