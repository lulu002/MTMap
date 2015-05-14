package com.hltc.dao;


import java.util.List;

import com.hltc.entity.UserPhotoAlbum;

public interface IUserPhotoAlbumDao extends GenericDao<UserPhotoAlbum>{
	/**
	 * 批量添加
	 * @param userPhotoAlbum
	 * @return
	 */
	public Boolean batchAdd(List<UserPhotoAlbum> userPhotoAlbum);
}
