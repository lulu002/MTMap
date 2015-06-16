package com.hltc.service.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

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
		restAPI.init(GlobalConstant.CLOOPEN_PRO_URL, "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(GlobalConstant.CLOOPEN_ACCOUNT_SID, GlobalConstant.CLOOPEN_AUTH_TOKEN);// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId(GlobalConstant.CLOOPEN_MAITIN_APP_ID);// 初始化应用ID
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
	private Boolean checkPwd(Long userId, String pwd){
		PwdHash ph = pwdHashDao.findById(userId);
		if(null != ph){
			String str = ph.getPwdSalt() + pwd.toUpperCase();
			return SecurityUtil.MD5(str).equals(ph.getPwdHash());
		}
		return false;
	}
	
	private Boolean createPwdHash(Long userId,String pwd) {
		PwdHash ph = new PwdHash();
		String salt = UUIDUtil.getUUID();
		ph.setUserId(userId);
		ph.setPwdSalt(salt);
		ph.setPwdHash(SecurityUtil.MD5(salt + pwd.toUpperCase()));
		pwdHashDao.saveOrUpdate(ph);
		return true;
	}
	
	private HashMap updatePwdHash(Long userId, String pwd){
		PwdHash ph = pwdHashDao.findByUserId(userId);
		HashMap setParams = new HashMap();
		HashMap whereParams = new HashMap();
		try{
			if(null == ph) return Result.fail(ErrorCode.PWD_HASH_NOT_EXIST);;
			setParams.put("pwd_hash", SecurityUtil.MD5(ph.getPwdSalt() + pwd.toUpperCase()));
			whereParams.put("ph_id", ph.getPhId());
			pwdHashDao.updateByShard(setParams, "pwd_hash", "user_id", userId, whereParams);
		}catch(Exception e){
			e.printStackTrace();
			return Result.fail(ErrorCode.DB_ERROR);
		}
		return Result.success();
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
	public Object sendVerifyCode(String to, Long userId, String type) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		HashMap<String, Object> setParams = new HashMap<String, Object>();
		TreeMap<String, Object> whereParams = new TreeMap<String, Object>();
		int exeResult = 0;
		String verifyCodeStr = generateVerifyCode();
		Boolean isNew = false;
		VerifyCode verifyCode = null;
		if(null == userId){
			verifyCode = verifyCodeDao.findByPhone(to);	
		}else{
			List list = verifyCodeDao.findByShard("verify_code", "user_id", userId, null);
			if(null != list && list.size() > 0) verifyCode = (VerifyCode) list.get(0);
		}
		 
		if(null == verifyCode){   
			verifyCode = new VerifyCode();
			setParams.put("phone", to);
			setParams.put("today_count", 1);
			setParams.put("user_id", null == userId ? (long)100 : userId);
			isNew = true;
		}else{
			Integer count = verifyCode.getTodayCount();
			if(!isDateEqual(new Date(), verifyCode.getCreateTime())){  //上次验证码创建时间不是今天
				setParams.put("today_count", 1);
			}else if(count < GlobalConstant.VERIFY_CDOE_MAX_COUNT_PERDAY  ){
				setParams.put("today_count", count + 1);
			}else{  //今日验证码超过次数限制
				return Result.fail(ErrorCode.VERIFY_CODE_COUNT_LIMIT);
			}
			whereParams.put("vc_id", verifyCode.getVcId());
		}
		
		setParams.put("create_time", System.currentTimeMillis());
		setParams.put("verify_code", verifyCodeStr);
	
		if("test".equalsIgnoreCase(GlobalConstant.ENV)) {
			if(isNew) exeResult = verifyCodeDao.saveBySQL(setParams, "verify_code");
			else	exeResult = verifyCodeDao.updateByShard(setParams, "verify_code", "user_id", verifyCode.getUserId(),whereParams);
			return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();  //测试环境不发送验证码
		}
		
		HashMap<String, Object> sendResult = restAPI.sendTemplateSMS(to, GlobalConstant.CLOOPEN_SMS_ID, new String[]{verifyCodeStr, GlobalConstant.VERIFY_CODE_VALID_TIME.toString()});
		if(null == sendResult || !"000000".equals(sendResult.get("statusCode"))){ //验证码发送失败  
			result = Result.fail(ErrorCode.VERIFY_CODE_SEND_FAIL);
		}else{
			if(isNew) exeResult = verifyCodeDao.saveBySQL(setParams, "verify_code");
			else	exeResult = verifyCodeDao.updateByShard(setParams, "verify_code","user_id", verifyCode.getUserId(),whereParams);
			result = exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success(); 
		}
		
		return  result; 
	}

	@Override
	public HashMap verifyByVerifyCode(String phoneNumber, String verifyCodeStr) {
		VerifyCode verifyCode = verifyCodeDao.findByPhone(phoneNumber);
		String code = null;  //数据库中存放的验证码
		
		if(null == verifyCode) return Result.fail(ErrorCode.DB_ERROR);
		
		code = verifyCode.getVerifyCode();
		if(null == code || "" == code|| !verifyCodeStr.equals(code) ){ //验证码错误
			return Result.fail(ErrorCode.VERIFY_CODE_WRONG);
		}else if(System.currentTimeMillis() - verifyCode.getCreateTime() >= GlobalConstant.VERIFY_CODE_VALID_TIME * 60000){ //验证码超时
			return Result.fail(ErrorCode.VERIFY_CODE_TIMEOUT);
		}
		
		HashMap<String, Object> setParams = new HashMap<String, Object>();
		HashMap<String, Object> whereParams = new HashMap<String, Object>();
		setParams.put("verify_code", "" );//验证成功将验证码置空
		whereParams.put("vc_id",verifyCode.getVcId());
		int exeResult = verifyCodeDao.updateByShard(setParams, "verify_code", "user_id", verifyCode.getUserId(), whereParams);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success(null);
	}

	@Override
	public User createUser(String phoneNumber, String pwd) {
		User user = new User();
		user.setNickName("改名吧"+phoneNumber);
		user.setPhone(phoneNumber);
		user.setCoverImg(GlobalConstant.DEFAULT_COVER_IMG);
		user.setPortrait(GlobalConstant.DEFAULT_POTRAIT);
		user.setPortraitSmall(GlobalConstant.DEFAULT_POTRAIT_SMALL);
		user.setCreateTime(System.currentTimeMillis());
		user = userDao.saveOrUpdateWithBack(user);
		if(null != user){
			createPwdHash(user.getUserId(), pwd);	
		}
		return user;
	}
	
	@Override
	public String generateAndSaveOrUpdateToken(Long userId) {
		int exeResult = 0;
		String tokenStr = UUIDUtil.getUUID();
		HashMap<String, Object> setParams = new HashMap<String, Object>();
		HashMap<String, Object> whereParams = new HashMap<String, Object>();
		Boolean isExist = false;
		Token token = null;
		
		if(null != userId){
			token = tokenDao.findByUserId(userId);
		}
		
		if(null != token) 	{
			isExist = true;
		}
		
		setParams.put("token", tokenStr);
		setParams.put("create_time", System.currentTimeMillis());
		
		if(isExist){
			whereParams.put("token_id", token.getTokenId());
			exeResult = tokenDao.updateByShard(setParams, "token", "user_id", token.getUserId(), whereParams);
		}else{
			token = new Token();
			setParams.put("user_id",null == userId ? (long)100 : userId);
			exeResult = tokenDao.saveBySQL(setParams, "token");
		}
		
		return exeResult == -1  ? "-1" : tokenStr;
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
	public HashMap login(String uniqueInfo, String pwd) {
		User user = userDao.findByUniqueInfo(uniqueInfo);
		if(null == user || !checkPwd(user.getUserId(), pwd)){
			return Result.fail(ErrorCode.ACCOUNT_AUTHORIZED_FAILED);
		}else{
			HashMap map = (HashMap)BeanUtil.beanToMap(user);
			map.put("token", generateAndSaveOrUpdateToken(user.getUserId()));
			return Result.success(map);
		}
	}

	@Override
	public HashMap loginByToken(Long userId, String tokenStr) {
		User user = userDao.findById(userId);
		if(null == user) return Result.fail(ErrorCode.ACCOUNT_AUTHORIZED_FAILED);
		
		HashMap<String, Object> whereParams = new HashMap<String, Object>();
		whereParams.put("token", tokenStr);
		List<Token>  list = tokenDao.findByShard("token", "user_id", userId, whereParams);
		if(null == list) return Result.fail(ErrorCode.DB_ERROR);
		int size = list.size();
		if(size == 0) return Result.fail(ErrorCode.ACCOUNT_AUTHORIZED_FAILED);
		if(size > 1) return Result.fail(ErrorCode.ITEM_REPEAT);
		Token token = list.get(0);
		if(System.currentTimeMillis() - token.getCreateTime() > GlobalConstant.TOKEN_VALID_TIME * 24 * 3600 * 1000){
			return Result.fail(ErrorCode.TOKEN_LOGIN_FAIL);
		}
		
		HashMap data = (HashMap)BeanUtil.beanToMap(user);
		data.put("token", tokenStr);
		return Result.success(data);
	}

	@Override
	public HashMap resetPassword(String phoneNumber, String pwd) {
		User user = userDao.findByPhone(phoneNumber);
		if(null == user)	return Result.fail(ErrorCode.PHONE_NOT_REGISTERED);
		return updatePwdHash(user.getUserId(), pwd);
	}
	
	@Override
	public HashMap resetPassword(long userId, String pwd) {
		User user = userDao.findById(userId);
		if(null == user)	return Result.fail(ErrorCode.USER_NOT_EXIST);
		return updatePwdHash(user.getUserId(), pwd);
	}

	@Override
	public HashMap verifyOnSettingPwd(Long userId, String verifyCodeStr) {
		HashMap whereParams = new HashMap();
		VerifyCode verifyCode = null;
		String code = null;
		whereParams.put("verify_code", verifyCodeStr);
		List list = verifyCodeDao.findByShard("verify_code", "user_id", userId, whereParams);
		if(null != list && list.size() > 0) verifyCode = (VerifyCode)list.get(0);
		if(null == verifyCode) return Result.fail(ErrorCode.DB_ERROR);
		
		code = verifyCode.getVerifyCode();
		if(null == code || "" == code|| !verifyCodeStr.equals(code) ){ //验证码错误
			return Result.fail(ErrorCode.VERIFY_CODE_WRONG);
		}else if(System.currentTimeMillis() - verifyCode.getCreateTime() >= GlobalConstant.VERIFY_CODE_VALID_TIME * 60000){ //验证码超时
			return Result.fail(ErrorCode.VERIFY_CODE_TIMEOUT);
		}
		
		HashMap set = new HashMap();
		set.put("verify_code", "");
		int exeResult = verifyCodeDao.updateByShard(set, "verify_code", "user_id", userId,null);
		return exeResult !=1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
}
