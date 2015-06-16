package com.hltc.entity;

/**
 * Praise entity. @author MyEclipse Persistence Tools
 */

public class Praise implements java.io.Serializable {

	// Fields

	private Long praiseId;
	private Long gid;
	private Long userId;
	private Long createTime;
	private Boolean isDeleted;

	// Constructors

	/** default constructor */
	public Praise() {
	}

	/** full constructor */
	public Praise(Long gid, Long userId, Long createTime, Boolean isDeleted) {
		this.gid = gid;
		this.userId = userId;
		this.createTime = createTime;
		this.isDeleted = isDeleted;
	}

	// Property accessors

	public Long getPraiseId() {
		return this.praiseId;
	}

	public void setPraiseId(Long praiseId) {
		this.praiseId = praiseId;
	}

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

}