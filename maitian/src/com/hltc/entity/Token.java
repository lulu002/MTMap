package com.hltc.entity;

/**
 * Token entity. @author MyEclipse Persistence Tools
 */

public class Token implements java.io.Serializable {

	// Fields

	private Long tokenId;
	private Long userId;
	private Long createTime;
	private String token;

	// Constructors

	/** default constructor */
	public Token() {
	}

	/** full constructor */
	public Token(Long userId, Long createTime, String token) {
		this.userId = userId;
		this.createTime = createTime;
		this.token = token;
	}

	// Property accessors

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}