package com.hltc.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.ISiteDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Site;
import com.hltc.entity.User;

@Repository("siteDao")
public class SiteDaoImpl extends GenericHibernateDao<Site> implements ISiteDao{

	@Override
	public List<Site> findByIds(List<String> ids) {
		if(null == ids || ids.size() == 0) return null;
		List<Site> sites = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from site where site_id in (");
		for(String id : ids){
			sql.append("'"+id+"',");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		try{
			session = getSession();
			sites = session.createSQLQuery(sql.toString()).addEntity(Site.class).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return sites;
	}
}
