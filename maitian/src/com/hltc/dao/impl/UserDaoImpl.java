package com.hltc.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
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
		Session session = null;
		List<User> users = null;
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery("select * from user where phone = ? union all select * from user where user_name = ?").addEntity(User.class);
			 				query.setParameter(0, uniqueInfo).setParameter(1, uniqueInfo);
			 users = query.list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public List<User> findByPhones(Collection phones) {
		Session session = null;
		List<User> users = null;
		try{
			session = getSession();
			users = session.createQuery("from User where phone in (:phones)").setParameterList("phones", phones).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		return users;
	}

	@Override
	public int updateNickName(Long userId, String nickName) {
		HashMap setParams = new HashMap();
		setParams.put("nick_name", nickName);
		return this.updateByShard(setParams, "user", "user_id", userId, null);
	}

	@Override
	public List<User> findByIds(List<Long> ids) {
		if(null == ids || ids.size() == 0) return null;
		List<User> users = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from `user` where user_id in (");
		for(Long id : ids){
			sql.append(id + ",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		try{
			session = getSession();
			users = session.createSQLQuery(sql.toString()).addEntity(User.class).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return users;
	}
}


