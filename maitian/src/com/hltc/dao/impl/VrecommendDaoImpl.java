package com.hltc.dao.impl;

import org.springframework.stereotype.Repository;

import com.hltc.dao.IVrecommendDao;
import com.hltc.entity.Vrecommend;

@Repository("vrecommendDao")
public class VrecommendDaoImpl extends GenericHibernateDao<Vrecommend> implements IVrecommendDao{
}


