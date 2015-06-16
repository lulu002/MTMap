package com.hltc.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IVerifyCodeDao;
import com.hltc.entity.VerifyCode;

@Repository("verifyCodeDao")
public class VerifyCodeDaoImpl extends GenericHibernateDao<VerifyCode> implements IVerifyCodeDao{
	@Override
	public VerifyCode findByPhone(String phone) {
		Session session = null;
		List<VerifyCode> list = null;
		
		try{
			session = getSession();
			list = getSession().createQuery("from VerifyCode where phone = ?").setParameter(0, phone).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		return list.size() > 0? list.get(0) : null;
	}
}
