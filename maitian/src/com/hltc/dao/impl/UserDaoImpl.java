package com.hltc.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hltc.dao.IUserDao;
import com.hltc.entity.User;

@Repository("userDao")
public class UserDaoImpl extends GenericHibernateDao<User> implements IUserDao{

	@Override
	public User findByPhone(String phone) {
		List<User> users  = getSession().createQuery("from User where phone = ?").setParameter(0, phone).list();
		return users.size() == 0 ? null : (User)users.get(0);
	}

	@Override
	public User findByUniqueInfo(String uniqueInfo) {
		List<User> users = getSession().createQuery("from User where phone = ? or userId = ? or userName = ?")
									.setParameter(0, uniqueInfo).setParameter(1, uniqueInfo).setParameter(2, uniqueInfo).list();
		return users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public List<User> findByPhones(Collection phones) {
		List<User> users = getSession().createQuery("from User where phone in (:phones)").setParameterList("phones", phones).list();
		return users.size() > 0 ? users : null;
	}
}
