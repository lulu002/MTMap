package com.hltc.dao.impl;


import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;



import com.hltc.dao.IFriendDao;
import com.hltc.entity.Friend;

@Repository("friendDao")
public class FriendDaoImpl extends GenericHibernateDao<Friend> implements IFriendDao{

	@Override
	public Friend findByUserId(String userId) {
		Session session = getSession();
		List list = session.createQuery("from Friend where userId = ?").setParameter(0, userId).list();
		session.close();
		return  list.size() > 0 ?  (Friend)list.get(0) : null;
	}

	@Override
	public Friend findByTwoId(String userId, String userFid) {
		Session session = getSession();
		List list = session.createQuery("from Friend where userId = ? and userFid = ?").setParameter(0, userId).setParameter(1, userFid).list();
		session.close();
		return list.size() > 0 ? (Friend)list.get(0) : null;
	}
}
