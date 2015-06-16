package com.hltc.dao.impl;


import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IPraiseDao;
import com.hltc.entity.Praise;

@Repository("praiseDao")
public class PraiseDaoImpl extends GenericHibernateDao<Praise> implements IPraiseDao{

	@Override
	public Praise findByGidAndUserId(Long gid, Long userId) {
		Session session = null;
		List<Praise> list = null;
		try{
			session = getSession();
			list = session.createQuery("from Praise where gid = ? and userId = ?").setParameter(0, gid).setParameter(1, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
