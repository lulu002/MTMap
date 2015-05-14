package com.hltc.dao.impl;


import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IPraiseDao;
import com.hltc.entity.Praise;

@Repository("praiseDao")
public class PraiseDaoImpl extends GenericHibernateDao<Praise> implements IPraiseDao{

	@Override
	public Praise findByGidAndUserId(String gid, String userId) {
		Session session = getSession();
		List<Praise> list = session.createQuery("from Praise where gid = ? and userId = ?").setParameter(0, gid).setParameter(1, userId).list();
		session.close();
		return list.size() > 0 ? list.get(0) : null;
	}

}
