package com.hltc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hltc.dao.IVerifyCodeDao;
import com.hltc.entity.VerifyCode;

@Repository("verifyCodeDao")
public class VerifyCodeDaoImpl extends GenericHibernateDao<VerifyCode> implements IVerifyCodeDao{

	@Override
	public VerifyCode findByPhone(String phone) {
		
		List<VerifyCode> list = getSession().createQuery("from VerifyCode where phone = ?").setParameter(0, phone).list();
		
		if(list.size() == 0) return null;
		
		return list.get(0);
	}
	
}
