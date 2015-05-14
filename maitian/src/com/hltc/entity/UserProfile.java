package com.hltc.entity;

import java.util.Date;

/**
 * UserProfile entity. @author MyEclipse Persistence Tools
 */

public class UserProfile implements java.io.Serializable {

	// Fields

	private String upId;
	private String userId;
	private Date birthday;
	private String email;
	private String gender;
	private Long registerTime;

	// Constructors

	/** default constructor */
	public UserProfile() {
	}

	/** minimal constructor */
	public UserProfile(String upId) {
		this.upId = upId;
	}

	/** full constructor */
	public UserProfile(String upId, String userId, Date birthday, String email,
			String gender, Long registerTime) {
		this.upId = upId;
		this.userId = userId;
		this.birthday = birthday;
		this.email = email;
		this.gender = gender;
		this.registerTime = registerTime;
	}

	// Property accessors

	public String getUpId() {
		return this.upId;
	}

	public void setUpId(String upId) {
		this.upId = upId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Long registerTime) {
		this.registerTime = registerTime;
	}

}