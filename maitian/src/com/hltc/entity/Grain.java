package com.hltc.entity;

/**
 * Grain entity. @author MyEclipse Persistence Tools
 */

public class Grain implements java.io.Serializable {

	// Fields

	private Long gid;
	private String mcateId;
	private String siteId;
	private Long userId;
	private Boolean isPublic;
	private String text;
	private Double lon;
	private Double lat;
	private Long createTime;
	private Boolean isDeleted;
	private Short recommend;

	// Constructors

	/** default constructor */
	public Grain() {
	}

	/** minimal constructor */
	public Grain(Long gid, Boolean isPublic) {
		this.gid = gid;
		this.isPublic = isPublic;
	}

	/** full constructor */
	public Grain(Long gid, String mcateId, String siteId, Long userId,
			Boolean isPublic, String text, Double lon, Double lat,
			Long createTime, Boolean isDeleted, Short recommend) {
		this.gid = gid;
		this.mcateId = mcateId;
		this.siteId = siteId;
		this.userId = userId;
		this.isPublic = isPublic;
		this.text = text;
		this.lon = lon;
		this.lat = lat;
		this.createTime = createTime;
		this.isDeleted = isDeleted;
		this.recommend = recommend;
	}

	// Property accessors

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
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

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
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

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Short getRecommend() {
		return this.recommend;
	}

	public void setRecommend(Short recommend) {
		this.recommend = recommend;
	}

}