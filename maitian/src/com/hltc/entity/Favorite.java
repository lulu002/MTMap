package com.hltc.entity;

/**
 * Favorite entity. @author MyEclipse Persistence Tools
 */

public class Favorite implements java.io.Serializable {

	// Fields

	private Long favorId;
	private Long userId;
	private Long gid;
	private Long createTime;
	private Boolean isDeleted;

	// Constructors

	/** default constructor */
	public Favorite() {
	}

	/** full constructor */
	public Favorite(Long userId, Long gid, Long createTime, Boolean isDeleted) {
		this.userId = userId;
		this.gid = gid;
		this.createTime = createTime;
		this.isDeleted = isDeleted;
	}

	// Property accessors

	public Long getFavorId() {
		return this.favorId;
	}

	public void setFavorId(Long favorId) {
		this.favorId = favorId;
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