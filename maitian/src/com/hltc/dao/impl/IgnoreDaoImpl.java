package com.hltc.dao.impl;


import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IIgnoreDao;
import com.hltc.entity.Ignore;

@Repository("ignoreDao")
public class IgnoreDaoImpl extends GenericHibernateDao<Ignore> implements IIgnoreDao{

	@Override
	public Ignore findByGidAndUserId(String gid, String userId) {
		Session session = getSession();
		List<Ignore> list = session.createQuery("from Ignore where gid = ? and userId = ?").setParameter(0, gid).setParameter(1, userId).list();
		session.close();
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
