package com.hltc.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;



import com.hltc.dao.IFriendDao;
import com.hltc.entity.Friend;

@Repository("friendDao")
public class FriendDaoImpl extends GenericHibernateDao<Friend> implements IFriendDao{

	@Override
	public Friend findByTwoId(Long userId, Long userFid) {
		Session session = null;
		List list = null;
		try{
			session = getSession();
			list = session.createQuery("from Friend where userId = ? and userFid = ?").setParameter(0, userId).setParameter(1, userFid).list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null == list || list.size() == 0 ?  null : (Friend)list.get(0);
	}

	@Override
	public List<Friend> findByUserIds(Long userId, Long[] ids) {
		List<Friend> friends = new ArrayList<Friend>();
		if(null == ids || ids.length == 0) return friends;
		
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from friend where user_id = ? and flag = '1'  and is_deleted = '0' and user_fid in( ");
		for(Long id : ids){
			sql.append(id + ",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql.toString()).addEntity(Friend.class);
			friends = query.setParameter(0, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return friends;
	}

	@Override
	public List<Friend> findAllByUserIds(Long userId, Long[] ids) {
		if(null == ids || ids.length == 0) return null;
		
		List<Friend> friends = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from friend where user_id = ? and user_fid in( ");
		for(Long id : ids){
			sql.append(id + ",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql.toString()).addEntity(Friend.class);
			friends = query.setParameter(0, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return friends;
	}

	@Override
	public List<Friend> findAddingByUserId(Long userId) {
		String sql1 = "select * from friend where user_id = ? and flag ='0'";
		String sql2 = "select * from friend where user_fid = ? and flag='0'";
		List<Friend> f1 = null;
		List<Friend> f2 = null;
		Session session = null;
		try{
			session = getSession();
			SQLQuery query1 = session.createSQLQuery(sql1).addEntity(Friend.class);
			SQLQuery query2 = session.createSQLQuery(sql2).addEntity(Friend.class);
			f1 = query1.setParameter(0, userId).list();
			f2 = query2.setParameter(0, userId).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		f1.addAll(f2);
		return f1;
	}
}
