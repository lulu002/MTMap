package com.hltc.dao;

import java.util.List;

import com.hltc.entity.Neglect;


public interface INeglectDao extends GenericDao<Neglect>{
	
	/**
	 * 麦粒查询
	 * @param gid 麦粒id
	 * @param userId 用户id 
	 * @return
	 */
	public Neglect findByGidAndUserId(Long gid, Long userId);
	
	/**
	 * 根据麦粒id集合查询忽略麦粒
	 * @param gids
	 * @param userId
	 * @return
	 */
	public List<Neglect> findByGidsAndUserId(List<Long> gids, Long userId);
}
