package com.hltc.entity;

/**
 * Favorite entity. @author MyEclipse Persistence Tools
 */

public class Favorite implements java.io.Serializable {

	// Fields

	private String favorId;
	private String userId;
	private String gid;
	private Long time;

	// Constructors

	/** default constructor */
	public Favorite() {
	}

	/** minimal constructor */
	public Favorite(String favorId) {
		this.favorId = favorId;
	}

	/** full constructor */
	public Favorite(String favorId, String userId, String gid, Long time) {
		this.favorId = favorId;
		this.userId = userId;
		this.gid = gid;
		this.time = time;
	}

	// Property accessors

	public String getFavorId() {
		return this.favorId;
	}

	public void setFavorId(String favorId) {
		this.favorId = favorId;
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