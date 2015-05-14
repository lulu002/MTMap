package com.hltc.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hltc.entity.Token;
import com.hltc.entity.User;

/**
 *  用户模块Service
 * @author Hengbin Chen
  */
public interface IUserService {
	/**
	 * 通过userId 获取用户信息
	 * @param userId
	 */
	public User getUserById(String userId);
	

	public Object sendVerifyCode(String to, String userId, String type);
	
	public Boolean isPhoneExist(String phone);
	
	public HashMap verifyByVerifyCode(String phoneNumber, String verifyCodeStr);
	
	public User createUser(String phoneNumber, String pwd);
	
	/**
	 * @return tokenStr
	 */
	public String generateToken(String userId);
	
	/**
	 * 临时token是否有效
	 * @return
	 */
	public Boolean isTmpTokenValid(String tokenStr);
	
	public Token findByToken(String tokenStr);
	
	public Boolean deleteToken(Token token);
	
	public HashMap login(String uniqueInfo, String pwd);
	
	public HashMap loginByToken(String userId, String token);
	
	public HashMap resetPassword(String phoneNumber, String pwd);
	
	public List<User> findUsersByPhones(Collection phones);
}
