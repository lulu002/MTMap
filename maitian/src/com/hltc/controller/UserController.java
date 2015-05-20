package com.hltc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


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


import com.google.gson.JsonArray;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.entity.Token;
import com.hltc.entity.User;
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
		HashMap map = new HashMap(), result = null;
		map.put("unique_info", String.class);
		map.put("pwd", String.class);
		result = SecurityUtil.parametersValidate(jobj, map);
		
		if(null == result.get("success")){
			return result;
		}
		
		return userService.login((String)jobj.get("unique_info"),(String) jobj.get("pwd"));
	} 
	
	/**
	 * 通过token登录
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/login/login_by_token.json", method=RequestMethod.POST)
	public @ResponseBody Object login_by_token(@RequestBody JSONObject jobj){
		return userService.loginByToken(jobj.getString("user_id"), jobj.getString("token"));
	}
	
	/**
	 * 登录 -> 忘记密码
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping(value="/login/forgot_password.json", method=RequestMethod.GET)
	public @ResponseBody Object forgotPassword(@RequestParam("phone_number") String phoneNumber){
		if(!userService.isPhoneExist(phoneNumber)){
			return Result.fail(ErrorCode.PHONE_NOT_REGISTERED);
		}
		return (HashMap)userService.sendVerifyCode(phoneNumber, null, null);
	}
	
	/**
	 * 登录 -> 重置密码
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/login/reset_password.json", method=RequestMethod.POST)
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
		if(userService.isPhoneExist(phoneNumber)){
			return  Result.fail(ErrorCode.PHONE_HAS_REGISTERED);
		}else{
			return (HashMap)userService.sendVerifyCode(phoneNumber, null, null);
		}
	}
	
	/**
	 * 注册 -> 校验验证码,  登录 -> 忘记密码 -> 校验密码
	 * @param phoneNumber
	 * @param verifyCodeStr
	 * @return tmp_token
	 */
	@RequestMapping(value={"/register/verify.json","/login/check_verify_code.json"}, method=RequestMethod.GET)
	public @ResponseBody Object verifyOnRegister(@RequestParam("phone_number") String phoneNumber, @RequestParam("verify_code") String verifyCodeStr){
		HashMap result = userService.verifyByVerifyCode(phoneNumber, verifyCodeStr);
		
		if("true".equals(result.get(Result.SUCCESS))){
			HashMap data = new HashMap();
			data.put("tmp_token", userService.generateToken(null));
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
		User user = null;
		String phoneNumber = (String)jobj.get("phone_number");
		if(userService.isPhoneExist(phoneNumber)){
			return Result.fail(ErrorCode.PHONE_HAS_REGISTERED);
		}
		
		Token token = userService.findByToken((String)jobj.get("tmp_token"));
		if(null == token || System.currentTimeMillis() - token.getCreateTime() > GlobalConstant.TMP_TOKEN_VALID_TIME * 60000){
			return Result.fail(ErrorCode.TOKEN_INVALID);
		}

		user = userService.createUser(phoneNumber, (String)jobj.getString("pwd"));
		if(null != user){
			userService.deleteToken(token);
			String tokenStr = userService.generateToken(user.getUserId());
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
		HashMap map = userService.loginByToken(jobj.getString("user_id"), jobj.getString("token"));
		if( null == map.get("success")){
			return map;
		}
		ArrayList<User> users = (ArrayList)userService.findUsersByPhones((Collection) jobj.get("phone_numbers"));
		
		return Result.success(users);
	}
}
