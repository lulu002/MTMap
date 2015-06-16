package com.hltc.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IRecommendDao;
import com.hltc.dao.IVersionDao;
import com.hltc.entity.Recommend;
import com.hltc.entity.Version;

@Repository("recommendDao")
public class RecommendDaoImpl extends GenericHibernateDao<Recommend> implements IRecommendDao{

}
