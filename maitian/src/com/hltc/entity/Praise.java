package com.hltc.entity;

/**
 * Praise entity. @author MyEclipse Persistence Tools
 */

public class Praise implements java.io.Serializable {

	// Fields

	private String praiseId;
	private String gid;
	private String userId;
	private Long time;

	// Constructors

	/** default constructor */
	public Praise() {
	}

	/** minimal constructor */
	public Praise(String praiseId) {
		this.praiseId = praiseId;
	}

	/** full constructor */
	public Praise(String praiseId, String gid, String userId, Long time) {
		this.praiseId = praiseId;
		this.gid = gid;
		this.userId = userId;
		this.time = time;
	}

	// Property accessors

	public String getPraiseId() {
		return this.praiseId;
	}

	public void setPraiseId(String praiseId) {
		this.praiseId = praiseId;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}