package com.hltc.dao;

import java.util.List;

import com.hltc.entity.Friend;

public interface IFriendDao extends GenericDao<Friend>{
	
	public Friend findByTwoId(Long userId, Long userFid);
	
	/**
	 * 在一个id集合中查找好友的集合
	 * @param userId
	 * @param ids
	 * @return
	 */
	public List<Friend> findByUserIds(Long userId, Long[] ids);

}
