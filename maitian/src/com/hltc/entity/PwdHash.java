package com.hltc.entity;

/**
 * PwdHash entity. @author MyEclipse Persistence Tools
 */

public class PwdHash implements java.io.Serializable {

	// Fields

	private String userId;
	private String pwdSalt;
	private String pwdHash;

	// Constructors

	/** default constructor */
	public PwdHash() {
	}

	/** minimal constructor */
	public PwdHash(String userId) {
		this.userId = userId;
	}

	/** full constructor */
	public PwdHash(String userId, String pwdSalt, String pwdHash) {
		this.userId = userId;
		this.pwdSalt = pwdSalt;
		this.pwdHash = pwdHash;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPwdSalt() {
		return this.pwdSalt;
	}

	public void setPwdSalt(String pwdSalt) {
		this.pwdSalt = pwdSalt;
	}

	public String getPwdHash() {
		return this.pwdHash;
	}

	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}

}