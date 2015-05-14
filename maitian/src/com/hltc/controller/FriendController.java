package com.hltc.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Friend;
import com.hltc.entity.User;
import com.hltc.service.IPushService;
import com.hltc.service.IUserService;
import com.hltc.util.UUIDUtil;


/**
 * 朋友模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/friend")
public class FriendController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IPushService pushService;
	@Autowired
	private IFriendDao friendDao;
	@Autowired
	private IUserDao userDao;
	
	@RequestMapping(value="/add_friend.json", method=RequestMethod.POST)
	public @ResponseBody Object addFriend(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 检验被加好友是否存在
		String toId = jobj.getString("to_id");
		User user = (User) userDao.findById(toId);
		if(null == user){
			return Result.fail(ErrorCode.USER_NOT_EXIST);
		}
	
		//step3 在friend表中创建记录
		Friend friend = friendDao.findByTwoId(userId, toId);
		if(null == friend){
			friend = new Friend(UUIDUtil.getUUID());
			friend.setUserId(userId);
			friend.setUserFid(toId);
			friend.setFlag("0");
			friendDao.saveOrUpdate(friend);
		}
		
		//step4 向友盟发送消息
		Map<String, String> map = new HashMap();
		map.put("from_id", userId);
		try {
			pushService.sendIOSCustomizedcast(toId, "您有新的好友请求", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}
	
	@RequestMapping(value="/agree.json", method=RequestMethod.POST)
	public @ResponseBody Object agree(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 检验被加好友是否存在
		String fromId = jobj.getString("from_id");
		User user = (User) userDao.findById(fromId);
		if(null == user){
			return Result.fail(ErrorCode.USER_NOT_EXIST);
		}
		
		//step3 在friend表中创建记录
		Friend fromFriend = friendDao.findByUserId(fromId);
		fromFriend.setFlag("1");
		friendDao.saveOrUpdate(fromFriend);
		
		Friend toFriend = new Friend(UUIDUtil.getUUID());
		toFriend.setUserId(userId);
		toFriend.setUserFid(fromId);
		toFriend.setFlag("1");
		friendDao.saveOrUpdate(toFriend);
			
		//step4 向友盟发送推送信息
		//TODO
		Map<String, String> map = new HashMap();
		map.put("to_id", userId);
		try {
			pushService.sendIOSCustomizedcast(fromId, "您有新的好友请求", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}
}
