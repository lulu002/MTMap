package com.hltc.entity;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */

public class Friend implements java.io.Serializable {

	// Fields

	private Long fid;
	private Long userId;
	private Long userFid;
	private Boolean isStar;
	private String remark;
	private String flag;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** full constructor */
	public Friend(Long userId, Long userFid, Boolean isStar, String remark,
			String flag) {
		this.userId = userId;
		this.userFid = userFid;
		this.isStar = isStar;
		this.remark = remark;
		this.flag = flag;
	}

	// Property accessors

	public Long getFid() {
		return this.fid;
	}

	public void setFid(Long fid) {
		this.fid = fid;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserFid() {
		return this.userFid;
	}

	public void setUserFid(Long userFid) {
		this.userFid = userFid;
	}

	public Boolean getIsStar() {
		return this.isStar;
	}

	public void setIsStar(Boolean isStar) {
		this.isStar = isStar;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}