package com.hltc.dao;

import com.hltc.entity.Visitor;

public interface IVisitorDao extends GenericDao<Visitor>{
	
	/**
	 * 获取某个ip地址当天的访客数量
	 * @param ip
	 * @param createTime
	 * @return
	 */
	public Integer countOfOneIp(Long ip, Long createTime);
}
