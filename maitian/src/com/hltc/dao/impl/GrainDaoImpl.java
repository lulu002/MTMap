package com.hltc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IGrainDao;
import com.hltc.dao.ITokenDao;
import com.hltc.entity.Grain;
import com.hltc.entity.Token;

@Repository("grainDao")
public class GrainDaoImpl extends GenericHibernateDao<Grain> implements IGrainDao{

	@Override
	public List findFriendsGrain(Long userId, String mtcateId,
			String cityCode, Double lon, Double lat, Double radius) {
		String sql = "SELECT DISTINCT g.gid AS grainId, u.user_id AS userId, g.lon AS lon, g.lat AS lat, u.portrait_small AS userSmallPortait "+
						   "FROM grain AS g, 	`user` AS u, friends AS f " + 
						   "WHERE " +
						   "f.user_id = ? AND " +
						   "f.user_fid = u.user_id AND " +
						   "f.user_fid = g.user_id AND " +
						   "f.flag = 1 AND " +
						   "g.mcate_id = ?";
		
		Session session = getSession();
		List result = session.createSQLQuery(sql).setParameter(0, userId).setParameter(1, mtcateId).list();
		session.close();
		return result;
	}

	@Override
	public List queryByCondition(JSONObject jobj) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select g.*  ")
		    .append("from grain as g,site as s, user as u ")
		    .append("where ")
		    .append("g.site_id = s.site_id ")
		    .append("and g.user_id = u.user_id ")
		    .append("ORDER BY g.user_id ");
		Object startRow = jobj.get("startRow");
		Object pageSize = jobj.get("pageSize");
		if(null != startRow){
			Integer size = null == pageSize ? 10 : (Integer)pageSize;
			sql.append("desc limit "+ startRow+","+ size);
		}
		Session session = getSession();
		
		List list = session.createSQLQuery(sql.toString()).addEntity(Grain.class).list();
		session.close();
		return list;
	}
	

	@Override
	public Integer countByCondition(JSONObject jobj) {
		StringBuffer sql = new StringBuffer();
		sql.append("select *  ")
		    .append("from grain as g,site as s, user as u ")
		    .append("where ")
		    .append("g.site_id = s.site_id ")
		    .append("and g.user_id = u.user_id ")
		    .append("ORDER BY g.user_id ");
		Session session = getSession();
		List list = session.createSQLQuery(sql.toString()).list();
		session.close();
		return list.size();
	}

	@Override
	public List<Grain> getVrecommendGrains(Long vid) {
		Session session = null;
		List<Grain> list = null;
		String sql = "select g.* from grain as g where g.recommend = '1' and g.is_deleted = 0 and not exists(select * from vrecommend as v where v.gid = g.gid and v.vid = ?) limit 0,20";
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql).addEntity(Grain.class);
			list = query.setParameter(0, vid).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return list;
	}
	
	@Override
	public List<Grain> getUrecommendGrains(Long userId){
		Session session = null;
		List<Grain> list = null;
		String sql = "select g.* from grain as g where g.recommend = '1' and g.is_deleted = 0 and g.user_id <> ? and not exists(select * from recommend as r where r.gid = g.gid and r.user_id = ?) limit 0,20";
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql).addEntity(Grain.class);
			list = query.setParameter(0, userId).setParameter(1, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return list;
	}
}
