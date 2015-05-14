package com.hltc.dao;


import com.hltc.entity.Praise;

public interface IPraiseDao extends GenericDao<Praise>{
	
	/**
	 * 通过麦粒id和userId来查询
	 * @param gid
	 * @param userId
	 * @return
	 */
	public Praise findByGidAndUserId(String gid, String userId);
}
