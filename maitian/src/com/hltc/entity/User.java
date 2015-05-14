package com.hltc.entity;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private String userId;
	private String userName;
	private String phone;
	private String portrait;
	private String coverImg;
	private String nickName;
	private String portraitSmall;
	private Long createTime;
	private Boolean isLogin;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String userId) {
		this.userId = userId;
	}

	/** full constructor */
	public User(String userId, String userName, String phone, String portrait,
			String coverImg, String nickName, String portraitSmall,
			Long createTime, Boolean isLogin) {
		this.userId = userId;
		this.userName = userName;
		this.phone = phone;
		this.portrait = portrait;
		this.coverImg = coverImg;
		this.nickName = nickName;
		this.portraitSmall = portraitSmall;
		this.createTime = createTime;
		this.isLogin = isLogin;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPortrait() {
		return this.portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getCoverImg() {
		return this.coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPortraitSmall() {
		return this.portraitSmall;
	}

	public void setPortraitSmall(String portraitSmall) {
		this.portraitSmall = portraitSmall;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsLogin() {
		return this.isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

}