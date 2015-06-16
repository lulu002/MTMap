package com.hltc.dao;

import com.hltc.entity.Favorite;

public interface IFavoriteDao extends GenericDao<Favorite>{
	
	public Favorite findByGidAndUserId(Long gid, Long userId);
}
