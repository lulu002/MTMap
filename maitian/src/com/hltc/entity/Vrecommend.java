package com.hltc.entity;

/**
 * Vrecommend entity. @author MyEclipse Persistence Tools
 */

public class Vrecommend implements java.io.Serializable {

	// Fields

	private Long vrecomId;
	private Long gid;
	private Long vid;
	private Long createTime;

	// Constructors

	/** default constructor */
	public Vrecommend() {
	}

	/** full constructor */
	public Vrecommend(Long gid, Long vid, Long createTime) {
		this.gid = gid;
		this.vid = vid;
		this.createTime = createTime;
	}

	// Property accessors

	public Long getVrecomId() {
		return this.vrecomId;
	}

	public void setVrecomId(Long vrecomId) {
		this.vrecomId = vrecomId;
	}

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getVid() {
		return this.vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}