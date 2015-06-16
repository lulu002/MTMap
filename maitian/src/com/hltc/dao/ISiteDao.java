package com.hltc.dao;


import java.util.List;

import com.hltc.entity.Site;

public interface ISiteDao extends GenericDao<Site>{
	
	/**
	 * 通过siteid数组获取Site集合
	 * @param siteids
	 * @return
	 */
	public List<Site> findByIds(List<String> siteids);
}
