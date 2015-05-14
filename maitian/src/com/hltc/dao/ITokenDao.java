package com.hltc.dao;

import com.hltc.entity.Token;

public interface ITokenDao extends GenericDao<Token>{
	public Token findByToken(String tokenStr);
	
	public Token findByUserId(String userId);
}
