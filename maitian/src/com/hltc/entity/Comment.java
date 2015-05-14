package com.hltc.entity;

/**
 * Comment entity. @author MyEclipse Persistence Tools
 */

public class Comment implements java.io.Serializable {

	// Fields

	private String cid;
	private String gid;
	private String content;
	private String tocid;
	private Long time;
	private String userId;

	// Constructors

	/** default constructor */
	public Comment() {
	}

	/** minimal constructor */
	public Comment(String cid) {
		this.cid = cid;
	}

	/** full constructor */
	public Comment(String cid, String gid, String content, String tocid,
			Long time, String userId) {
		this.cid = cid;
		this.gid = gid;
		this.content = content;
		this.tocid = tocid;
		this.time = time;
		this.userId = userId;
	}

	// Property accessors

	public String getCid() {
		return this.cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTocid() {
		return this.tocid;
	}

	public void setTocid(String tocid) {
		this.tocid = tocid;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}