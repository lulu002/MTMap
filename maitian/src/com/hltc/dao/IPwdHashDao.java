package com.hltc.dao;

import com.hltc.entity.PwdHash;

public interface IPwdHashDao extends GenericDao<PwdHash>{
	
	public PwdHash findByUserId(String userId);
}
