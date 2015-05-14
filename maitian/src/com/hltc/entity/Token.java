package com.hltc.entity;

/**
 * Token entity. @author MyEclipse Persistence Tools
 */

public class Token implements java.io.Serializable {

	// Fields

	private String tokenId;
	private String userId;
	private Long createTime;
	private String token;

	// Constructors

	/** default constructor */
	public Token() {
	}

	/** minimal constructor */
	public Token(String tokenId) {
		this.tokenId = tokenId;
	}

	/** full constructor */
	public Token(String tokenId, String userId, Long createTime, String token) {
		this.tokenId = tokenId;
		this.userId = userId;
		this.createTime = createTime;
		this.token = token;
	}

	// Property accessors

	public String getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
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