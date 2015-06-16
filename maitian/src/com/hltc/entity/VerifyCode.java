package com.hltc.entity;

/**
 * VerifyCode entity. @author MyEclipse Persistence Tools
 */

public class VerifyCode implements java.io.Serializable {

	// Fields

	private Long vcId;
	private Long userId;
	private String phone;
	private String verifyCode;
	private Long createTime;
	private Integer todayCount;

	// Constructors

	/** default constructor */
	public VerifyCode() {
	}

	/** full constructor */
	public VerifyCode(Long userId, String phone, String verifyCode,
			Long createTime, Integer todayCount) {
		this.userId = userId;
		this.phone = phone;
		this.verifyCode = verifyCode;
		this.createTime = createTime;
		this.todayCount = todayCount;
	}

	// Property accessors

	public Long getVcId() {
		return this.vcId;
	}

	public void setVcId(Long vcId) {
		this.vcId = vcId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVerifyCode() {
		return this.verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getTodayCount() {
		return this.todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}

}