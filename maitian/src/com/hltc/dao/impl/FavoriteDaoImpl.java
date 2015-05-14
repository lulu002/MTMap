package com.hltc.dao.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IFavoriteDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Favorite;
import com.hltc.entity.User;

@Repository("favoriteDao")
public class FavoriteDaoImpl extends GenericHibernateDao<Favorite> implements IFavoriteDao{

	@Override
	public Favorite findByGidAndUserId(String gid, String userId) {
		Session session = getSession();
		List<Favorite> list = session.createQuery("from Favorite where gid = ? and userId = ?").setParameter(0, gid).setParameter(1, userId).list();
 		session.close();
 		
		return list.size() > 0 ? list.get(0) : null;
	}


}
