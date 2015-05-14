package com.hltc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.hltc.dao.ITokenDao;
import com.hltc.entity.Token;

@Repository("tokenDao")
public class TokenDaoImpl extends GenericHibernateDao<Token> implements ITokenDao{

	@Override
	public Token findByToken(String tokenStr) {
		List<Token> list = getSession().createQuery("from Token where token = ? ").setParameter(0, tokenStr).list();
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Token findByUserId(String userId) {
		List<Token> list = getSession().createQuery("from Token where userId = ? ").setParameter(0, userId).list();
		return list.size() > 0 ? list.get(0) : null;
	}
}
