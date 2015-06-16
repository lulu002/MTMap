package com.hltc.entity;

/**
 * UserPhotoAlbum entity. @author MyEclipse Persistence Tools
 */

public class UserPhotoAlbum implements java.io.Serializable {

	// Fields

	private Long upaId;
	private Long gid;
	private Long userId;
	private String photo;
	private Long createTime;
	private String photoSmall;

	// Constructors

	/** default constructor */
	public UserPhotoAlbum() {
	}

	/** full constructor */
	public UserPhotoAlbum(Long gid, Long userId, String photo, Long createTime,
			String photoSmall) {
		this.gid = gid;
		this.userId = userId;
		this.photo = photo;
		this.createTime = createTime;
		this.photoSmall = photoSmall;
	}

	// Property accessors

	public Long getUpaId() {
		return this.upaId;
	}

	public void setUpaId(Long upaId) {
		this.upaId = upaId;
	}

	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getPhotoSmall() {
		return this.photoSmall;
	}

	public void setPhotoSmall(String photoSmall) {
		this.photoSmall = photoSmall;
	}

}