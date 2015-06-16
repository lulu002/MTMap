package com.hltc.dao.impl;


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
		Session session = getSession();
		List list = session.createQuery("from Friend where userId = ? and userFid = ?").setParameter(0, userId).setParameter(1, userFid).list();
		session.close();
		return list.size() > 0 ? (Friend)list.get(0) : null;
	}

	@Override
	public List<Friend> findByUserIds(Long userId, Long[] ids) {
		if(null == ids || ids.length == 0) return null;
		
		List<Friend> friends = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from friend where user_id = ? and flag = '1' and user_fid in( ");
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
}
