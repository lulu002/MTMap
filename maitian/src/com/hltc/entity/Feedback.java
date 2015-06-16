package com.hltc.entity;

/**
 * Feedback entity. @author MyEclipse Persistence Tools
 */

public class Feedback implements java.io.Serializable {

	// Fields

	private Long feedId;
	private Long userId;
	private String content;
	private Long createTime;
	private Boolean isDealed;
	private String email;

	// Constructors

	/** default constructor */
	public Feedback() {
	}

	/** full constructor */
	public Feedback(Long userId, String content, Long createTime,
			Boolean isDealed, String email) {
		this.userId = userId;
		this.content = content;
		this.createTime = createTime;
		this.isDealed = isDealed;
		this.email = email;
	}

	// Property accessors

	public Long getFeedId() {
		return this.feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsDealed() {
		return this.isDealed;
	}

	public void setIsDealed(Boolean isDealed) {
		this.isDealed = isDealed;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}