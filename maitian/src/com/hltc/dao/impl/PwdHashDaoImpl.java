package com.hltc.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;
import com.hltc.dao.IPwdHashDao;
import com.hltc.entity.PwdHash;

@Repository("pwdHashDao")
public class PwdHashDaoImpl extends GenericHibernateDao<PwdHash> implements IPwdHashDao{

	@Override
	public PwdHash findByUserId(String userId) {
		List<PwdHash> list = getSession().createQuery("from PwdHash where userId = ? ").setParameter(0, userId).list();
		return list.size() > 0 ? list.get(0) : null;
	}

}
