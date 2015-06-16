package com.hltc.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.hltc.dao.ITokenDao;
import com.hltc.entity.Token;

@Repository("tokenDao")
public class TokenDaoImpl extends GenericHibernateDao<Token> implements ITokenDao{

	@Override
	public Token findByToken(String tokenStr) {
		Session session = null;
		List<Token> list = new ArrayList<Token>();
		try{
			session = getSession();
			list = session.createQuery("from Token where token = ? ").setParameter(0, tokenStr).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Token findByUserId(Long userId) {
		Session session = null;
		List<Token> list = new ArrayList<Token>();
		try{
			session = getSession();
			list = session.createQuery("from Token where userId = ? ").setParameter(0, userId).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Token findATestToken() {
       Session session = getSession();
       List<Token> list = session.createQuery("from Token where userId like '1000000%'").list();
       Integer size = list.size();
       Integer rand = (int)(Math.random()*size);
       session.close();
       
		return size == 0 ? null : list.get(rand);
	}
}
