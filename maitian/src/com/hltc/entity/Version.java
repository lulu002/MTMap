package com.hltc.entity;

/**
 * Version entity. @author MyEclipse Persistence Tools
 */

public class Version implements java.io.Serializable {

	// Fields

	private Short vid;
	private String version;
	private Long createTime;
	private String log;

	// Constructors

	/** default constructor */
	public Version() {
	}

	/** full constructor */
	public Version(String version, Long createTime, String log) {
		this.version = version;
		this.createTime = createTime;
		this.log = log;
	}

	// Property accessors

	public Short getVid() {
		return this.vid;
	}

	public void setVid(Short vid) {
		this.vid = vid;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
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

}