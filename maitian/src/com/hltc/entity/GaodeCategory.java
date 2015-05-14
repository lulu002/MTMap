package com.hltc.entity;

/**
 * GaodeCategory entity. @author MyEclipse Persistence Tools
 */

public class GaodeCategory implements java.io.Serializable {

	// Fields

	private String gcateId;
	private String name;
	private String mcateId;

	// Constructors

	/** default constructor */
	public GaodeCategory() {
	}

	/** minimal constructor */
	public GaodeCategory(String gcateId) {
		this.gcateId = gcateId;
	}

	/** full constructor */
	public GaodeCategory(String gcateId, String name, String mcateId) {
		this.gcateId = gcateId;
		this.name = name;
		this.mcateId = mcateId;
	}

	// Property accessors

	public String getGcateId() {
		return this.gcateId;
	}

	public void setGcateId(String gcateId) {
		this.gcateId = gcateId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMcateId() {
		return this.mcateId;
	}

	public void setMcateId(String mcateId) {
		this.mcateId = mcateId;
	}

}