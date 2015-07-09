package com.hltc.dao;

import java.util.List;

import com.hltc.entity.Friend;

public interface IFriendDao extends GenericDao<Friend>{
	
	public Friend findByTwoId(Long userId, Long userFid);
	
	/**
	 * 在一个id集合中查找好友的集合，是双方都为好友的集合
	 * @param userId
	 * @param ids
	 * @return
	 */
	public List<Friend> findByUserIds(Long userId, Long[] ids);
	
	/**
	 * 在一个id集合中查找好友集合，包括已被删除和未加成功的好友
	 * @param userId
	 * @param ids
	 * @return
	 */
	public List<Friend> findAllByUserIds(Long userId, Long[] ids);
	
	/**
	 * 获取正在添加的好友，即：我想加别人为好友别人未回，别人想加我为好友我未回
	 * @param userId
	 * @return
	 */
	public List<Friend> findAddingByUserId(Long userId);
}
