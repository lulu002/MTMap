package com.hltc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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


import com.google.gson.JsonArray;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.LogUtil;

import static com.hltc.util.SecurityUtil.*;

/**
 * 授权模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/auth")
public class AuthController {
	@Autowired
	private IUserService userService;
	/**
	 * 获取ossToken
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/oss_token.json", method=RequestMethod.POST)
	public @ResponseBody Object login_by_token(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"user_id","token","content"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		result = userService.loginByToken(jobj.getString("user_id"), jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
	
		JSONObject o = new JSONObject();
		
		try {
			o.put("ossToken", generateOSSToken("wxGYeoOqFGIikopt", "eQyS38ArhJo0fIotIuLoiz0FCx0J4N", jobj.getString("content")));
		} catch (Exception e) {
			LogUtil.error(e.getMessage()); 
			e.printStackTrace();
		}
		return Result.success(o);
	}
}
