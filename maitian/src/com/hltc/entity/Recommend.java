package com.hltc.entity;

/**
 * Recommend entity. @author MyEclipse Persistence Tools
 */

public class Recommend implements java.io.Serializable {

	// Fields

	private Long recomId;
	private Long userId;
	private Long gid;
	private Long createTime;

	// Constructors

	/** default constructor */
	public Recommend() {
	}

	/** full constructor */
	public Recommend(Long userId, Long gid, Long createTime) {
		this.userId = userId;
		this.gid = gid;
		this.createTime = createTime;
	}

	// Property accessors

	public Long getRecomId() {
		return this.recomId;
	}

	public void setRecomId(Long recomId) {
		this.recomId = recomId;
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

}