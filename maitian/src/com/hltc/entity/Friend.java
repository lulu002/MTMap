package com.hltc.entity;

/**
 * Friend entity. @author MyEclipse Persistence Tools
 */

public class Friend implements java.io.Serializable {

	// Fields

	private String fid;
	private String userId;
	private String userFid;
	private String isStar;
	private String remark;
	private String flag;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** minimal constructor */
	public Friend(String fid) {
		this.fid = fid;
	}

	/** full constructor */
	public Friend(String fid, String userId, String userFid, String isStar,
			String remark, String flag) {
		this.fid = fid;
		this.userId = userId;
		this.userFid = userFid;
		this.isStar = isStar;
		this.remark = remark;
		this.flag = flag;
	}

	// Property accessors

	public String getFid() {
		return this.fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFid() {
		return this.userFid;
	}

	public void setUserFid(String userFid) {
		this.userFid = userFid;
	}

	public String getIsStar() {
		return this.isStar;
	}

	public void setIsStar(String isStar) {
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