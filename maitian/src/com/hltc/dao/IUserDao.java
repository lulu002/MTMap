package com.hltc.dao;

import java.util.Collection;
import java.util.List;

import com.hltc.entity.User;

public interface IUserDao extends GenericDao<User>{
	/**
	 * 通过手机号码查找用户
	 * @param phone
	 * @return
	 */
	public User findByPhone(String phone);
	
	/**
	 * 通过用户的唯一信息查找，例如userId、phone、username等
	 * @param info
	 * @return
	 */
	public User findByUniqueInfo(String uniqueInfo);
	
	/**
	 * 通过手机号批量查找用户
	 * @param phones
	 * @return 如果查找失败则返回null
	 */
	public List<User> findByPhones(Collection phones);
	
	/**
	 * 修改昵称
	 * @param userId
	 * @param nickName
	 * @return 返回影响的条数，如果为-1,则数据库修改失败
	 */
	public int updateNickName(Long userId, String nickName);
	
	/**
	 * 批量获取用户信息
	 * @param ids
	 * @return
	 */
	public List<User> findByIds(List<Long> uids);
}
