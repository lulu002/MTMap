package com.hltc.entity;

/**
 * Comment entity. @author MyEclipse Persistence Tools
 */

public class Comment implements java.io.Serializable {

	// Fields

	private Long cid;
	private Long gid;
	private String content;
	private Long tocid;
	private Long createTime;
	private Long userId;
	private Boolean isDeleted;

	// Constructors

	/** default constructor */
	public Comment() {
	}

	/** minimal constructor */
	public Comment(Long gid) {
		this.gid = gid;
	}

	/** full constructor */
	public Comment(Long gid, String content, Long tocid, Long createTime,
			Long userId, Boolean isDeleted) {
		this.gid = gid;
		this.content = content;
		this.tocid = tocid;
		this.createTime = createTime;
		this.userId = userId;
		this.isDeleted = isDeleted;
	}

	// Property accessors

	public Long getCid() {
		return this.cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTocid() {
		return this.tocid;
	}

	public void setTocid(Long tocid) {
		this.tocid = tocid;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}