package com.hltc.dao;

import com.hltc.entity.Ignore;

public interface IIgnoreDao extends GenericDao<Ignore>{
	
	/**
	 * 麦粒查询
	 * @param gid 麦粒id
	 * @param userId 用户id 
	 * @return
	 */
	public Ignore findByGidAndUserId(String gid, String userId);
}
