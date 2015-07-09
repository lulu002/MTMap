package com.hltc.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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
	public List findFriendsGrain(List<Long> uids, String mtcateId,
			String cityCode, Double lon, Double lat, Double radius) {
		Session session = null;
		List result = null;
		StringBuilder sql = new StringBuilder("select * from (");
		sql.append("select g.gid,g.text,g.user_id,u.portrait,g.site_id,u.nick_name,g.mcate_id,");
		sql.append("6378.138*2*asin(sqrt(pow(sin( (g.lat*pi()/180- :lat*pi()/180)/2),2)+cos(g.lat*pi()/180)*cos(:lat*pi()/180)* pow(sin( (g.lon*pi()/180-:lon*pi()/180)/2),2)))*1000 as distance ");
		sql.append("from `user` as u join grain1 as g where u.user_id in(");
		for(Long uid : uids){
			sql.append(uid + ",");
		}
		sql.deleteCharAt(sql.length() -1);
		sql.append(") and u.user_id = g.user_id and g.city_code = :cityCode");
		if(null != mtcateId){
			sql.append(" and mcate_id = :mtcateId");
		}
		sql.append(") as rst");
		
		sql.append(" where rst.distance < :radius");
		
		try {
			session = getSession();
			Query query = session.createSQLQuery(sql.toString()).setParameter("lat", lat).setParameter("lon", lon)
			.setParameter("cityCode", cityCode).setParameter("radius", radius);
			if(null != mtcateId){
				query.setParameter("mtcateId", mtcateId);
			}
			result = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
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
		String sql = "select g.* from grain as g where g.recommend = 1 and IFNULL(g.is_deleted,0) = 0 and not exists(select * from vrecommend as v where v.gid = g.gid and v.vid = ?) limit 0,20";
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
		String sql = "select g.* from grain as g where g.recommend = '1' and IFNULL(g.is_deleted,0) = 0 and g.user_id <> ? and not exists(select * from recommend as r where r.gid = g.gid and r.user_id = ?) limit 0,20";
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

	@Override
	public Integer getCountByCate(Long userId, String cateExpression) {
		Session session = null;
		Integer count = null;
		String sql = "SELECT count(*) as count from grain1 as g where g.user_id = ? and g.mcate_id like ?";
		try{
			session = getSession();
			BigInteger result = (BigInteger)session.createSQLQuery(sql).setParameter(0, userId).setParameter(1, cateExpression).uniqueResult();
			if(null != result){
				count = result.intValue();
			}
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}finally{
			session.close();
		}
		return count;
	}
	
	public static void main(String[] args) {
		GrainDaoImpl dao = new GrainDaoImpl();
		List<Grain> list = dao.getUrecommendGrains((long)500001);
		
	}
}
