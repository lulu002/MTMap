package com.hltc.dao.impl;

import java.math.BigInteger;
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
import com.hltc.dao.IVisitorDao;
import com.hltc.entity.User;
import com.hltc.entity.Visitor;

@Repository("visitorDao")
public class VisitorDaoImpl extends GenericHibernateDao<Visitor> implements IVisitorDao{

	@Override
	public Integer countOfOneIp(Long ip, Long createTime) {
		Session session = null;
		Integer count = 0;
		String sql = "select count(vid) from visitor where ip = ? and create_time > ?";
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql);
			BigInteger result = (BigInteger) query.setParameter(0, ip).setParameter(1, createTime).uniqueResult();
			count = result.intValue();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return count;
	}
}


