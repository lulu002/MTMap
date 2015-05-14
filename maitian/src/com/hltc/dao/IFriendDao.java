package com.hltc.dao;

import com.hltc.entity.Friend;

public interface IFriendDao extends GenericDao<Friend>{
	
	public Friend findByUserId(String userId);
	
	public Friend findByTwoId(String userId, String userFid);

}
