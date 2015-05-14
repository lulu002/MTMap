package com.hltc.entity;

/**
 * Feedback entity. @author MyEclipse Persistence Tools
 */

public class Feedback implements java.io.Serializable {

	// Fields

	private String feedId;
	private String userId;
	private String content;
	private Long time;
	private String isDealed;

	// Constructors

	/** default constructor */
	public Feedback() {
	}

	/** minimal constructor */
	public Feedback(String feedId) {
		this.feedId = feedId;
	}

	/** full constructor */
	public Feedback(String feedId, String userId, String content, Long time,
			String isDealed) {
		this.feedId = feedId;
		this.userId = userId;
		this.content = content;
		this.time = time;
		this.isDealed = isDealed;
	}

	// Property accessors

	public String getFeedId() {
		return this.feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getIsDealed() {
		return this.isDealed;
	}

	public void setIsDealed(String isDealed) {
		this.isDealed = isDealed;
	}

}