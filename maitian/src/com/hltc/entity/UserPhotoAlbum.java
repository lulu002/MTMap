package com.hltc.entity;

/**
 * UserPhotoAlbum entity. @author MyEclipse Persistence Tools
 */

public class UserPhotoAlbum implements java.io.Serializable {

	// Fields

	private String upaId;
	private String gid;
	private String userId;
	private String photo;
	private Long time;
	private String photoSmall;

	// Constructors

	/** default constructor */
	public UserPhotoAlbum() {
	}

	/** minimal constructor */
	public UserPhotoAlbum(String upaId) {
		this.upaId = upaId;
	}

	/** full constructor */
	public UserPhotoAlbum(String upaId, String gid, String userId,
			String photo, Long time, String photoSmall) {
		this.upaId = upaId;
		this.gid = gid;
		this.userId = userId;
		this.photo = photo;
		this.time = time;
		this.photoSmall = photoSmall;
	}

	// Property accessors

	public String getUpaId() {
		return this.upaId;
	}

	public void setUpaId(String upaId) {
		this.upaId = upaId;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getPhotoSmall() {
		return this.photoSmall;
	}

	public void setPhotoSmall(String photoSmall) {
		this.photoSmall = photoSmall;
	}

}