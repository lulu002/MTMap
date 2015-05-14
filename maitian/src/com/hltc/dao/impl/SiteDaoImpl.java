package com.hltc.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hltc.dao.ISiteDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Site;
import com.hltc.entity.User;

@Repository("siteDao")
public class SiteDaoImpl extends GenericHibernateDao<Site> implements ISiteDao{

}
