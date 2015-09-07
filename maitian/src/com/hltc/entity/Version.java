package com.hltc.entity;

/**
 * Version entity. @author MyEclipse Persistence Tools
 */

public class Version implements java.io.Serializable {

	// Fields

	private Short vid;
	private String versionName;
	private Long createTime;
	private String log;
	private Short versionCode;

	// Constructors

	/** default constructor */
	public Version() {
	}

	/** full constructor */
	public Version(String versionName, Long createTime, String log,
			Short versionCode) {
		this.versionName = versionName;
		this.createTime = createTime;
		this.log = log;
		this.versionCode = versionCode;
	}

	// Property accessors

	public Short getVid() {
		return this.vid;
	}

	public void setVid(Short vid) {
		this.vid = vid;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Short getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(Short versionCode) {
		this.versionCode = versionCode;
	}

}