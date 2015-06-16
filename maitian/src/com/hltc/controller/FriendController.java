package com.hltc.controller;

import static com.hltc.util.SecurityUtil.parametersValidate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
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
import com.hltc.service.IFriendService;
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
	private IFriendService friendService;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IFriendDao friendDao;
	
	@RequestMapping(value="/add_friend_backyard.json", method=RequestMethod.POST)
	public @ResponseBody Object addFriendOnBackYard(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userIdA","userIdB"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		String userIdA = jobj.getString("userIdA"),
				  userIdB = jobj.getString("userIdB");
		if(null == userDao.findById(userIdA)){
			return Result.fail(ErrorCode.USER_NOT_EXIST, "userIdA don't exist");
		}
		
		if(null == userDao.findById(userIdB)){
			return Result.fail(ErrorCode.USER_NOT_EXIST, "userIdB don't exist");
		}
		//step1 在friend表中创建记录
		return friendService.addFriend(jobj.getLong("userIdA"), jobj.getLong("userIdB"));
	}
	/**
	 * 添加好友
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/add_friend.json", method=RequestMethod.POST)
	public @ResponseBody Object addFriend(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, "toId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 检验被加好友是否存在
		Long toId = jobj.getLong("toId");
		User user = (User) userDao.findById(toId);
		if(null == user)	return Result.fail(ErrorCode.USER_NOT_EXIST);
	
		//step3 在friend表中创建记录
		Friend friend = friendDao.findByTwoId(userId, toId);
		if(null == friend){
			friend = new Friend();
			friend.setUserId(userId);
			friend.setUserFid(toId);
			friend.setFlag("0");
			friendDao.save(friend);
		}
		
		//step4 向友盟发送消息
		Map<String, Object> map = new HashMap();
		map.put("fromId", userId);
		try {
			pushService.sendIOSCustomizedcast(toId+"", "您有新的好友请求", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}
	
	/**
	 * 同意好友添加请求
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/agree.json", method=RequestMethod.POST)
	public @ResponseBody Object agree(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, "fromId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 检验请求方是否存在
		Long fromId = jobj.getLong("fromId");
		User user = (User) userDao.findById(fromId);
		if(null == user)	return Result.fail(ErrorCode.USER_NOT_EXIST);
		
		//step3 在friend表中创建记录
		List list = friendDao.findByShard("friend", "user_id", fromId, null);
		if(null == list || list.size()==0) return Result.fail(ErrorCode.DB_ERROR);
		Friend fromFriend = (Friend) list.get(0);
		HashMap setParams = new HashMap();
		setParams.put("flag", "1");
		friendDao.updateByShard(setParams, "friend", "user_id", fromId, null);
		
		Friend toFriend = new Friend();
		setParams.clear();
		setParams.put("user_id", userId);
		setParams.put("user_fid", fromId);
		setParams.put("flag", "1");
		friendDao.saveBySQL(setParams, "friend");
			
		//step4 向友盟发送推送信息
		Map<String, Object> map = new HashMap();
		map.put("toId", userId);
		try {
			pushService.sendIOSCustomizedcast(fromId+"", "您有新的好友请求", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success();
	}
	
	@RequestMapping(value="/add_friend_batch.json", method=RequestMethod.POST)
	public @ResponseBody Object addFriendBatch(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userIdA"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"userIdBatch"}, true, JSONArray.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		List<Long> userIdBatch = (List<Long>)JSONArray.toCollection(jobj.getJSONArray("userIdBatch"), String.class);
		return friendService.addFriendBatch(jobj.getLong("userIdA"), userIdBatch);
	}
}
