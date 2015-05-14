package com.hltc.entity;

/**
 * Grain entity. @author MyEclipse Persistence Tools
 */

public class Grain implements java.io.Serializable {

	// Fields

	private String gid;
	private String mcateId;
	private String siteId;
	private String userId;
	private String isPublic;
	private String text;
	private Double lon;
	private Double lat;
	private Long time;
	private Boolean isDeleted;

	// Constructors

	/** default constructor */
	public Grain() {
	}

	/** minimal constructor */
	public Grain(String gid, String isPublic) {
		this.gid = gid;
		this.isPublic = isPublic;
	}

	/** full constructor */
	public Grain(String gid, String mcateId, String siteId, String userId,
			String isPublic, String text, Double lon, Double lat, Long time,
			Boolean isDeleted) {
		this.gid = gid;
		this.mcateId = mcateId;
		this.siteId = siteId;
		this.userId = userId;
		this.isPublic = isPublic;
		this.text = text;
		this.lon = lon;
		this.lat = lat;
		this.time = time;
		this.isDeleted = isDeleted;
	}

	// Property accessors

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getMcateId() {
		return this.mcateId;
	}

	public void setMcateId(String mcateId) {
		this.mcateId = mcateId;
	}

	public String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
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

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}