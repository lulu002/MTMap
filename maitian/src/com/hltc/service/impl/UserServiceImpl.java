package com.hltc.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.IPwdHashDao;
import com.hltc.dao.ITokenDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IVerifyCodeDao;
import com.hltc.entity.PwdHash;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.entity.VerifyCode;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.SecurityUtil;
import com.hltc.util.UUIDUtil;

@Service("userService")
public class UserServiceImpl implements IUserService{
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IVerifyCodeDao verifyCodeDao;
	@Autowired
	private ITokenDao tokenDao;
	@Autowired
	private IPwdHashDao pwdHashDao;
	private CCPRestSDK restAPI = new CCPRestSDK();
	
	public UserServiceImpl() {
		restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("aaf98f8949d126580149d1efb6ca0095", "1b975439287449bf89e3cc0e3c20d55b");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("aaf98f8949d575140149da8e079202ac");// 初始化应用ID
	}
	
	/**
	 * 生成验证码
	 * @return
	 */
	private String generateVerifyCode(){
		if("test".equalsIgnoreCase(GlobalConstant.ENV)) return "8888";
		return (int)((Math.random()*9+1)*1000) + "";
	}
	
	/**
	 * 生成VerifyCode表主键  17位时间+随机8位数
	 * @return
	 */
	private String generateVerifyCodeId(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //设置日期格式
		return sdf.format(new Date()).toString() + (int)((Math.random()*9+1)*10000000);
	}
/**
 * 生成用户ID	手机号码前7位+17位时间+8位随机数
 * @param cityzip
 * @return
 */
	private String generateUserId(String phoneNumber){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return phoneNumber.substring(0, 7) + sdf.format(new Date()) + (int)((Math.random()*9 + 1)*10000000);
	}
	
	private Boolean isDateEqual(Date date, Long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		String dateStr = formatter.format(date);
		String timeStr = formatter.format(time);
		return dateStr.compareTo(timeStr) == 0 ? true : false;
	}
	
	//检查密码是否正确
	private Boolean checkPwd(String userId, String pwd){
		PwdHash ph = pwdHashDao.findById(userId);
		if(null != ph){
			String str = ph.getPwdSalt() + pwd.toUpperCase();
			return SecurityUtil.MD5(str).equals(ph.getPwdHash());
		}
		return false;
	}
	
	private Boolean createPwdHash(String userId, String pwd) {
		PwdHash ph = new PwdHash();
		String salt = UUIDUtil.getUUID();
		ph.setUserId(userId);
		ph.setPwdSalt(salt);
		ph.setPwdHash(SecurityUtil.MD5(salt + pwd.toUpperCase()));
		pwdHashDao.saveOrUpdate(ph);
		return true;
	}
	
	private HashMap updatePwdHash(String userId, String pwd){
		PwdHash ph = pwdHashDao.findByUserId(userId);
		
		if(null != ph){
			ph.setPwdHash(SecurityUtil.MD5(ph.getPwdSalt() + pwd.toUpperCase()));
			pwdHashDao.saveOrUpdate(ph);
			return Result.success(null);
		}else{
			return Result.fail(ErrorCode.PWD_HASH_NOT_EXIST);
		}
	}
	
	
	//批量生成大规模的测试用户 手机号码t开头
	private void createUserBatch(){
		
	}
	
	public Boolean isPhoneExist(String phone){
		User user = userDao.findByPhone(phone);
		return null != user ? true : false;
	}
	
	
	@Override
	public User getUserById(String userId) {
		return userDao.findById(userId);
	}

	
	@Override
	public Object sendVerifyCode(String to, String userId, String type) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String verifyCodeStr = generateVerifyCode();
		
		VerifyCode verifyCode = verifyCodeDao.findByPhone(to);
		if(null == verifyCode){   
			verifyCode = new VerifyCode();
			verifyCode.setVcId(generateVerifyCodeId());
			verifyCode.setPhone(to);
			verifyCode.setTodayCount(1); 
		}else{
			Integer count = verifyCode.getTodayCount();
			if(!isDateEqual(new Date(), verifyCode.getCreateTime())){  //上次验证码创建时间不是今天
				verifyCode.setTodayCount(1); 
			}else if(count < GlobalConstant.VERIFY_CDOE_MAX_COUNT_PERDAY  ){
				verifyCode.setTodayCount(count + 1);
			}else{  //今日验证码超过次数限制
				return Result.fail(ErrorCode.VERIFY_CODE_COUNT_LIMIT);
			}
		}
		
		verifyCode.setCreateTime(System.currentTimeMillis());
		verifyCode.setVerifyCode(verifyCodeStr);
		if(null != userId){
			verifyCode.setUserId(userId);
		}
		
		if("test".equalsIgnoreCase(GlobalConstant.ENV)) {
			verifyCodeDao.saveOrUpdate(verifyCode);  
			return Result.success();  //测试环境不发送验证码
		}
		
		HashMap<String, Object> sendResult = restAPI.sendTemplateSMS(to, "8859", new String[]{verifyCodeStr, GlobalConstant.VERIFY_CODE_VALID_TIME.toString()});
		if(null == sendResult || !"000000".equals(sendResult.get("statusCode"))){ //验证码发送失败  
			result = Result.fail(ErrorCode.VERIFY_CODE_SEND_FAIL);
		}else{
			verifyCodeDao.saveOrUpdate(verifyCode);  //TODO  需要返回值
			result = Result.success();
		}
		
		return  result; 
	}

	@Override
	public HashMap verifyByVerifyCode(String phoneNumber, String verifyCodeStr) {
		VerifyCode verifyCode = verifyCodeDao.findByPhone(phoneNumber);
		String code = null;  //数据库中存放的验证码
		
		if(null !=verifyCode ){
			code = verifyCode.getVerifyCode();
		}
		
		if(null == code || "" == code|| !verifyCodeStr.equals(code) ){ //验证码错误
			return Result.fail(ErrorCode.VERIFY_CODE_WRONG);
		}else if(System.currentTimeMillis() - verifyCode.getCreateTime() >= GlobalConstant.VERIFY_CODE_VALID_TIME * 60000){ //验证码超时
			return Result.fail(ErrorCode.VERIFY_CODE_TIMEOUT);
		}
		
		verifyCode.setVerifyCode(""); //验证成功将验证码置空
		return Result.success(null);
	}

	@Override
	public User createUser(String phoneNumber, String pwd) {
		User user = new User();
		String userId = generateUserId(phoneNumber);
		user.setNickName("改名吧"+phoneNumber);
		user.setPhone(phoneNumber);
		user.setUserId(userId);
		user.setCoverImg(GlobalConstant.DEFAULT_COVER_IMG);
		user.setPortrait(GlobalConstant.DEFAULT_POTRAIT);
		user.setPortraitSmall(GlobalConstant.DEFAULT_POTRAIT_SMALL);
		user.setCreateTime(System.currentTimeMillis());
		userDao.saveOrUpdate(user);
		createPwdHash(userId, pwd);
		return user;
	}
	
	@Override
	public String generateToken(String userId) {
		String tokenStr = UUIDUtil.getUUID();
		Token token = null;
		if(null != userId){
			token = tokenDao.findByUserId(userId);
			if(null == token){
				token = new Token(UUIDUtil.getUUID());
			}
			token.setUserId(userId);
		}else{
			token = new Token(UUIDUtil.getUUID());
		}
		token.setToken(tokenStr);
		token.setCreateTime(System.currentTimeMillis());

		tokenDao.saveOrUpdate(token);
		return tokenStr;
	}

	@Override
	public Boolean isTmpTokenValid(String tokenStr) {
		Token token = tokenDao.findByToken(tokenStr);
		
		return null == token 
				   || !tokenStr.equals(token.getToken()) 
				   || System.currentTimeMillis() - token.getCreateTime() > GlobalConstant.TMP_TOKEN_VALID_TIME * 60000 ? false : true;
	}

	@Override
	public Token findByToken(String tokenStr) {
		return tokenDao.findByToken(tokenStr);
	}

	@Override
	public Boolean deleteToken(Token token) {
		try {
			tokenDao.delete(token);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public HashMap login(String uniqueInfo, String pwd) {
		User user = userDao.findByUniqueInfo(uniqueInfo);
		if(null == user || !checkPwd(user.getUserId(), pwd)){
			return Result.fail(ErrorCode.ACCOUNT_AUTHORIZED_FAILED);
		}else{
			HashMap map = (HashMap)BeanUtil.beanToMap(user);
			map.put("token", generateToken(user.getUserId()));
			return Result.success(map);
		}
	}

	@Override
	public HashMap loginByToken(String userId, String tokenStr) {
		Token token = tokenDao.findByToken(tokenStr);
		if(null != token && token.getUserId().equals(userId) && 
				System.currentTimeMillis() - token.getCreateTime() < GlobalConstant.TOKEN_VALID_TIME * 24 * 3600 * 1000){
			return Result.success(null);
		}
		return Result.fail(ErrorCode.TOKEN_LOGIN_FAIL);
	}

	@Override
	public HashMap resetPassword(String phoneNumber, String pwd) {
		User user = userDao.findByPhone(phoneNumber);
		if(null == user){
			return Result.fail(ErrorCode.PHONE_NOT_REGISTERED);
		}
		
		return updatePwdHash(user.getUserId(), pwd);
	}

	@Override
	public List<User> findUsersByPhones(Collection phones) {
		return userDao.findByPhones(phones);
	}

}
