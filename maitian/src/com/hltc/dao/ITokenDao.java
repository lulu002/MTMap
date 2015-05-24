package com.hltc.dao;

import com.hltc.entity.Token;

public interface ITokenDao extends GenericDao<Token>{
	public Token findByToken(String tokenStr);
	
	public Token findByUserId(String userId);
	
	/**
	 * 查找一个测试用户的token信息，userId以100000 开头的
	 * @return
	 */
	public Token findATestToken();
}
