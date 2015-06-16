package com.hltc.entity;

/**
 * Visitor entity. @author MyEclipse Persistence Tools
 */

public class Visitor implements java.io.Serializable {

	// Fields

	private Long vid;
	private Long ip;
	private Long createTime;

	// Constructors

	/** default constructor */
	public Visitor() {
	}

	/** full constructor */
	public Visitor(Long ip, Long createTime) {
		this.ip = ip;
		this.createTime = createTime;
	}

	// Property accessors

	public Long getVid() {
		return this.vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public Long getIp() {
		return this.ip;
	}

	public void setIp(Long ip) {
		this.ip = ip;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}