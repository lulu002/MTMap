package com.hltc.entity;

/**
 * MtCategory entity. @author MyEclipse Persistence Tools
 */

public class MtCategory implements java.io.Serializable {

	// Fields

	private String mcateId;
	private String name;
	private String icon;
	private String pid;
	private String hasChild;

	// Constructors

	/** default constructor */
	public MtCategory() {
	}

	/** minimal constructor */
	public MtCategory(String mcateId) {
		this.mcateId = mcateId;
	}

	/** full constructor */
	public MtCategory(String mcateId, String name, String icon, String pid,
			String hasChild) {
		this.mcateId = mcateId;
		this.name = name;
		this.icon = icon;
		this.pid = pid;
		this.hasChild = hasChild;
	}

	// Property accessors

	public String getMcateId() {
		return this.mcateId;
	}

	public void setMcateId(String mcateId) {
		this.mcateId = mcateId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getHasChild() {
		return this.hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

}