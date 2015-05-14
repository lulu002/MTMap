package com.hltc.dao;

import com.hltc.entity.VerifyCode;

public interface IVerifyCodeDao extends GenericDao<VerifyCode>{
	public VerifyCode findByPhone(String phone);
}
