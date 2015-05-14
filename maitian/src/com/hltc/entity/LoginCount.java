package com.hltc.entity;

/**
 * LoginCount entity. @author MyEclipse Persistence Tools
 */

public class LoginCount implements java.io.Serializable {

	// Fields

	private String lcId;
	private String ip;
	private Long time;
	private String userId;
	private String location;

	// Constructors

	/** default constructor */
	public LoginCount() {
	}

	/** minimal constructor */
	public LoginCount(String lcId) {
		this.lcId = lcId;
	}

	/** full constructor */
	public LoginCount(String lcId, String ip, Long time, String userId,
			String location) {
		this.lcId = lcId;
		this.ip = ip;
		this.time = time;
		this.userId = userId;
		this.location = location;
	}

	// Property accessors

	public String getLcId() {
		return this.lcId;
	}

	public void setLcId(String lcId) {
		this.lcId = lcId;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}