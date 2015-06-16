package com.hltc.dao;

import com.hltc.entity.Neglect;


public interface INeglectDao extends GenericDao<Neglect>{
	
	/**
	 * 麦粒查询
	 * @param gid 麦粒id
	 * @param userId 用户id 
	 * @return
	 */
	public Neglect findByGidAndUserId(Long gid, Long userId);
}
