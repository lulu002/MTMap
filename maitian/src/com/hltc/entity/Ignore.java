package com.hltc.entity;

/**
 * Ignore entity. @author MyEclipse Persistence Tools
 */

public class Ignore implements java.io.Serializable {

	// Fields

	private String ignoreId;
	private String userId;
	private String gid;
	private Long time;

	// Constructors

	/** default constructor */
	public Ignore() {
	}

	/** minimal constructor */
	public Ignore(String ignoreId) {
		this.ignoreId = ignoreId;
	}

	/** full constructor */
	public Ignore(String ignoreId, String userId, String gid, Long time) {
		this.ignoreId = ignoreId;
		this.userId = userId;
		this.gid = gid;
		this.time = time;
	}

	// Property accessors

	public String getIgnoreId() {
		return this.ignoreId;
	}

	public void setIgnoreId(String ignoreId) {
		this.ignoreId = ignoreId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}