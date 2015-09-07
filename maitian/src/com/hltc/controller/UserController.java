package com.hltc.controller;

import static com.hltc.util.SecurityUtil.parametersValidate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.IFeedbackDao;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.ITokenDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IVerifyCodeDao;
import com.hltc.dao.IVersionDao;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.entity.VerifyCode;
import com.hltc.entity.Version;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.LogUtil;
import com.hltc.util.SecurityUtil;

/**
 * 用户模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private ITokenDao tokenDao;
	@Autowired
	private IVerifyCodeDao verifyCodeDao;
	@Autowired
	private IFeedbackDao feedbackDao;
	@Autowired
	private IVersionDao versionDao;
	@Autowired
	private IFriendDao friendDao;
	
	/**
    * 用户登陆方法
    */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(String username, String password, Model model){
	    System.out.println("登陆");
	    
	    User user  = userService.getUserById("001");

	    model.addAttribute("result",user.getCoverImg());
		return "pages/user/loginResult.jsp";
	}
	
	@RequestMapping(value="/info/{userId}",method=RequestMethod.GET)
	public @ResponseBody Object getUserInfo(@PathVariable String userId){
		JSONObject jsonObject =new JSONObject();
		
		jsonObject.put("msg","成功");
		
		User user = userService.getUserById(userId);
		
//		ModelAndView mav = new ModelAndView("user/info.jsp",map);
		return user;
	}
	
	/**
	 * 登录
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/login/login.json", method=RequestMethod.POST)
	public @ResponseBody Object login(@RequestBody JSONObject jobj){
		Map result = parametersValidate(jobj, new String[]{"unique_info","pwd"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		return userService.login(jobj.getString("unique_info"), jobj.getString("pwd"));
	} 
	
	/**
	 * 通过token登录
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/login/login_by_token.json", method=RequestMethod.POST)
	public @ResponseBody Object login_by_token(@RequestBody JSONObject jobj){
		//参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class,Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		return userService.loginByToken(jobj.getLong("userId"), jobj.getString("token"));
	}
	
	/**
	 * 登录 -> 忘记密码 -> 发送验证码
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping(value="/login/forget/verify_code.json", method=RequestMethod.GET)
	public @ResponseBody Object forgotPassword(@RequestParam("phone_number") String phoneNumber){
		if(!userService.isPhoneExist(phoneNumber)){
			return Result.fail(ErrorCode.PHONE_NOT_REGISTERED);
		}
		return (HashMap)userService.sendVerifyCode(phoneNumber, null, null);
	}
	
	/**
	 * 设置 -> 修改密码 -> 发送验证码 
	 * @param jobj  userId,token,phone
	 * @return
	 */
	@RequestMapping(value="/delelte/password/verify_code.json", method=RequestMethod.POST)
	public @ResponseBody Object sendVerifyCodeOnSettingPwd(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","phone"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		
		//step2 验证手机号是否为该用户
		HashMap data = (HashMap) result.get("data");
		String phone = null;
		if(null != data){
			phone = (String)data.get("phone");
		}
		if(null != phone && phone.equals(jobj.getString("phone"))){
			//step3 发送验证码
			return userService.sendVerifyCode(jobj.getString("phone"), userId, null);
		}else{
			return Result.fail(ErrorCode.PHONE_WRONG);
		}
	}
	
	/**
	 * 设置 ->修改密码 -> 校验验证码
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/delelte/password/verify.json", method = RequestMethod.POST)
	public @ResponseBody Object verifyOnSettingPwd(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","verifyCode"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		//step3 校验验证码
		return userService.verifyOnSettingPwd(userId, jobj.getString("verifyCode"));
	}
	
	/**
	 * 设置->修改密码 -> 重置密码
	 * @param jobj userId,token,pwd
	 * @return
	 */
	@RequestMapping(value="/delelte/password/reset.json", method = RequestMethod.POST)
	public @ResponseBody Object resetPwdOnSettingPwd(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","pwd"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		//step3 设置密码
		return userService.resetPassword(userId, jobj.getString("pwd"));
	}
	
	@RequestMapping(value="/delelte/feedback.json", method = RequestMethod.POST)
	public @ResponseBody Object feedback(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","content","email"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		//step3 用户反馈
		HashMap setParams = new HashMap();
		setParams.put("user_id", userId);
		setParams.put("content", jobj.getString("content"));
		setParams.put("email", jobj.getString("email"));
		setParams.put("create_time", System.currentTimeMillis());
		int exeResult = feedbackDao.saveBySQL(setParams, "feedback");
		return exeResult != 1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	/**
	 * 设置 -> 检测更新
	 * @return
	 */
	@RequestMapping(value="/delelte/latest_version.json", method = RequestMethod.GET)
	public @ResponseBody Object getLatestVersion(){
		Version version = versionDao.findLatesVersion();
		return null == version? Result.fail(ErrorCode.DB_ERROR) : Result.success(BeanUtil.beanToMap(version));
	}
	
	
	/**
	 * 登录 -> 重置密码
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/login/forget/reset_password.json", method=RequestMethod.POST)
	public @ResponseBody Object resetPassword(@RequestBody JSONObject jobj){
		if(!userService.isTmpTokenValid(jobj.getString("tmp_token"))){
			return Result.fail(ErrorCode.TOKEN_INVALID);
		}
		
		return userService.resetPassword(jobj.getString("phone_number"), jobj.getString("pwd"));
	}
	
	/**
	 * 注册 ->发送验证码到用户填写的手机上
	 * @param phoneNumber
	 * @return 
	 */
	@RequestMapping(value="/register/verify_code.json", method=RequestMethod.GET)
	public @ResponseBody Object sendVerifyCodeOnRegister(@RequestParam("phone_number") String phoneNumber){
		//step0 参数验证
		if(userService.isPhoneExist(phoneNumber)){
			return  Result.fail(ErrorCode.PHONE_HAS_REGISTERED);
		}else{
			return (HashMap)userService.sendVerifyCode(phoneNumber, null, null);
		}
	}
	
	/**
	 * 注册 -> 校验验证码,  
	 * 登录 -> 忘记密码 -> 校验密码
	 * @param phoneNumber
	 * @param verifyCodeStr
	 * @return tmp_token
	 */
	@RequestMapping(value={"/register/verify.json","/login/forget/verify.json"}, method=RequestMethod.GET)
	public @ResponseBody Object verifyOnRegister(@RequestParam("phone_number") String phoneNumber, @RequestParam("verify_code") String verifyCodeStr){
		HashMap result = userService.verifyByVerifyCode(phoneNumber, verifyCodeStr);
		
		if("true".equals(result.get(Result.SUCCESS))){
			HashMap data = new HashMap();
			data.put("tmp_token", userService.generateAndSaveOrUpdateToken(null));
			result =  Result.success(data);
		}
		return result;
	}
	
	/**
	 * 注册 -> 创建新用户
	 * @param phoneNumber
	 * @param pwd
	 * @param tmp_token
	 * @return
	 */
	@RequestMapping(value="/register/new_user.json", method=RequestMethod.POST)
	public @ResponseBody Object createUserOnRegister(@RequestBody JSONObject jobj){
		//参数验证
		Map result = parametersValidate(jobj, new String[]{"phone_number","pwd","tmp_token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		User user = null;
		String phoneNumber = jobj.getString("phone_number");
		if(userService.isPhoneExist(phoneNumber)){
			return Result.fail(ErrorCode.PHONE_HAS_REGISTERED);
		}
		
		Token token = userService.findByToken(jobj.getString("tmp_token"));
		if(null == token || System.currentTimeMillis() - token.getCreateTime() > GlobalConstant.TMP_TOKEN_VALID_TIME * 60000){
			return Result.fail(ErrorCode.TOKEN_INVALID);
		}

		user = userService.createUser(phoneNumber, (String)jobj.getString("pwd"));
		if(null != user){
			HashMap<String, Object> whereParams = new HashMap<String, Object>();
			whereParams.put("token_id", token.getTokenId());
			tokenDao.deleteByShard("token", "user_id", token.getUserId(), whereParams);
			String tokenStr = userService.generateAndSaveOrUpdateToken(user.getUserId());
			HashMap map = (HashMap) BeanUtil.beanToMap(user);
			map.put("token", tokenStr);
			return Result.success(map);
		}else{
			return Result.fail(ErrorCode.USRE_CREATE_FAIL, null);
		}
	}
	
	/**
	 * 获取手机联系人中已注册用户信息
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/register/check_contact.json", method=RequestMethod.POST)
	public @ResponseBody Object checkContact(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"phoneNumbers"}, true, JSONArray.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		
		//step2 获取通讯录好友
		List<User> users = userService.getNewFriendByPhones(userId, (Collection) jobj.get("phoneNumbers"));
		if(null==users) users = new ArrayList();
		return Result.success(users);
	}
	
	
	@RequestMapping(value="/delelte/update_nickname.json",method=RequestMethod.POST)
	public @ResponseBody Object updateNickName(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","nickName"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		
		int exeResult = userDao.updateNickName(userId, jobj.getString("nickName"));
		if(exeResult == -1) return Result.fail(ErrorCode.DB_ERROR);
			
		return Result.success();
	}
}
