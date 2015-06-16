package com.hltc.entity;

/**
 * Neglect entity. @author MyEclipse Persistence Tools
 */

public class Neglect implements java.io.Serializable {

	// Fields

	private Long neglectId;
	private Long userId;
	private Long gid;
	private Long createTime;
	private Boolean isDeleted;

	// Constructors

	/** default constructor */
	public Neglect() {
	}

	/** full constructor */
	public Neglect(Long userId, Long gid, Long createTime, Boolean isDeleted) {
		this.userId = userId;
		this.gid = gid;
		this.createTime = createTime;
		this.isDeleted = isDeleted;
	}

	// Property accessors

	public Long getNeglectId() {
		return this.neglectId;
	}

	public void setNeglectId(Long neglectId) {
		this.neglectId = neglectId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
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