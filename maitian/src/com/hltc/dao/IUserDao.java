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
	
	
	public List<User> findByPhones(Collection phones);
}
