package com.hltc.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.hltc.dao.IVersionDao;
import com.hltc.entity.Version;

@Repository("versionDao")
public class VersionDaoImpl extends GenericHibernateDao<Version> implements IVersionDao{

	@Override
	public Version findLatesVersion() {
		Version version = null;
		Session session = null;
		try {
			session = getSession();
			version = (Version)session.createSQLQuery("select * from version order by create_time desc limit 0,1").addEntity(Version.class).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return version;
	}
}
