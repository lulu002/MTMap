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
	private Boolean isDeleted;
	private Boolean isMeAdd;
	private String text;
	private Long createTime;

	// Constructors

	/** default constructor */
	public Friend() {
	}

	/** full constructor */
	public Friend(Long userId, Long userFid, Boolean isStar, String remark,
			String flag, Boolean isDeleted, Boolean isMeAdd, String text,
			Long createTime) {
		this.userId = userId;
		this.userFid = userFid;
		this.isStar = isStar;
		this.remark = remark;
		this.flag = flag;
		this.isDeleted = isDeleted;
		this.isMeAdd = isMeAdd;
		this.text = text;
		this.createTime = createTime;
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

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsMeAdd() {
		return this.isMeAdd;
	}

	public void setIsMeAdd(Boolean isMeAdd) {
		this.isMeAdd = isMeAdd;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}