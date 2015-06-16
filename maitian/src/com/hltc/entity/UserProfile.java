package com.hltc.entity;

/**
 * UserProfile entity. @author MyEclipse Persistence Tools
 */

public class UserProfile implements java.io.Serializable {

	// Fields

	private Long upId;
	private Long userId;
	private Long birthday;
	private String email;
	private String gender;
	private Long registerTime;

	// Constructors

	/** default constructor */
	public UserProfile() {
	}

	/** full constructor */
	public UserProfile(Long userId, Long birthday, String email, String gender,
			Long registerTime) {
		this.userId = userId;
		this.birthday = birthday;
		this.email = email;
		this.gender = gender;
		this.registerTime = registerTime;
	}

	// Property accessors

	public Long getUpId() {
		return this.upId;
	}

	public void setUpId(Long upId) {
		this.upId = upId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Long birthday) {
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