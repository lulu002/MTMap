package com.hltc.dao.impl;


import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.INeglectDao;
import com.hltc.entity.Neglect;

@Repository("neglectDao")
public class NeglectDaoImpl extends GenericHibernateDao<Neglect> implements INeglectDao{

	@Override
	public Neglect findByGidAndUserId(Long gid, Long userId) {
		Session session = null;
		List<Neglect> list = null;
		try{
			session = getSession();
			list = session.createQuery("from Neglect where gid = ? and userId = ?").setParameter(0, gid).setParameter(1, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
