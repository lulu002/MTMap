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
	
	/**
	 * 发送验证码
	 * @param to 发送的手机号
	 * @param userId 发送的userId
	 * @param type 发送类型,是语音还是短信
	 * @return
	 */
	public Object sendVerifyCode(String to, Long userId, String type);
	
	public Boolean isPhoneExist(String phone);
	
	public HashMap verifyByVerifyCode(String phoneNumber, String verifyCodeStr);
	
	public User createUser(String phoneNumber, String pwd);
	
	/**
	 * 生成并存储token，没有异常，返回tokenStr，DB错误返回-1
	 * @return 
	 */
	public String generateAndSaveOrUpdateToken(Long userId);
	
	/**
	 * 临时token是否有效
	 * @return
	 */
	public Boolean isTmpTokenValid(String tokenStr);
	
	public Token findByToken(String tokenStr);
	
	public HashMap login(String uniqueInfo, String pwd);
	
	public HashMap loginByToken(Long userId, String token);
	
	public HashMap resetPassword(String phoneNumber, String pwd);
	public HashMap resetPassword(long userId, String pwd);
	
	/**
	 * 设置 -》 修改密码 -》校验手机验证码
	 * @param userId
	 * @param verifyCode
	 * @return
	 */
	public HashMap verifyOnSettingPwd(Long userId, String verifyCode);
}
