package com.hltc.controller;

import static com.hltc.util.SecurityUtil.parametersValidate;

import java.util.ArrayList;
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
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Friend;
import com.hltc.entity.User;
import com.hltc.service.IFriendService;
import com.hltc.service.IGrainService;
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
	private IGrainService grainService;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IFriendDao friendDao;
	@Autowired
	private IGrainDao grainDao;
	
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
		Map result = parametersValidate(jobj, new String[]{"userId","toId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","text","remark"}, true, String.class);
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
		String text = jobj.getString("text");
		String remark = jobj.getString("remark");
		Long createTime = System.currentTimeMillis();
		if(null == friend){
			friend = new Friend();
			friend.setUserId(userId);
			friend.setUserFid(toId);
			friend.setFlag("0");
			friend.setIsDeleted(false);
			friend.setIsMeAdd(true);
			friend.setText(text);
			friend.setCreateTime(createTime);
			friend.setRemark(remark);
			friendDao.save(friend);
		}else{
			if("1".equals(friend.getFlag())) return Result.fail(ErrorCode.FRIEND_EXIST);
			HashMap setParams = new HashMap();
			setParams.put("text", text);
			setParams.put("create_time", createTime);
			setParams.put("remark",remark);
			friendDao.updateByShard(setParams, "friend", "user_id", userId, null);
		}
		
		//step4 向友盟发送消息
		Map<String, String> map = new HashMap();
		map.put("pUid", userId+"");   //点赞者id
		try {
			pushService.sendIOSCustomizedcast(toId+"", "您有新的好友请求", map);
			pushService.sendAndroidCustomizedcast(toId+"","您有新的好友请求", map);
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
		setParams.put("is_deleted", "0");
		
		friendDao.updateByShard(setParams, "friend", "user_id", fromId, null);
		
		Friend toFriend = new Friend();
		setParams.clear();
		setParams.put("user_id", userId);
		setParams.put("user_fid", fromId);
		setParams.put("flag", "1");
		setParams.put("is_deleted", "0");
		setParams.put("create_time", System.currentTimeMillis());
		friendDao.saveBySQL(setParams, "friend");
			
		//step4 向友盟发送推送信息
		Map<String, String> map = new HashMap();
		map.put("toId", userId+"");
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
	
	@RequestMapping(value="remark.json", method=RequestMethod.POST)
	public @ResponseBody Object remark(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","fuserId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","remark"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;

		//step1 查找朋友
		Long userId = jobj.getLong("userId");
		Long userFid = jobj.getLong("fuserId");
		Friend friend = friendDao.findByTwoId(userId, userFid);
		if(null == friend || friend.getIsDeleted() || !"1".equals(friend.getFlag())) return Result.fail(ErrorCode.NOT_FRIEND);
		
		HashMap setParams = new HashMap();
		setParams.put("remark", jobj.getString("remark"));
		HashMap whereParams = new HashMap();
		whereParams.put("user_fid", userFid);
		int exeResult = friendDao.updateByShard(setParams, "friend", "user_id", userId, whereParams);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	@RequestMapping(value="/personal/mainInfo.json", method=RequestMethod.POST)
	public @ResponseBody Object getFriendPersonalInfo(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","fuserId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 查找朋友
		Long userId = jobj.getLong("userId");
		Long userFid = jobj.getLong("fuserId");
		Friend friend = friendDao.findByTwoId(userId, userFid);
		if(null == friend || friend.getIsDeleted() || !"1".equals(friend.getFlag())) return Result.fail(ErrorCode.NOT_FRIEND);
		
		//step2 get UserInfo
		User user = userDao.findById(userFid);
		if(null == user) return Result.fail(ErrorCode.USER_NOT_EXIST);
	   	
		//step3 composite data
		HashMap rstData = new HashMap();
		
		HashMap userInfo = new HashMap();
	   	userInfo.put("userId", user.getUserId());
	   	userInfo.put("portrait", user.getPortrait());
	   	userInfo.put("nickName", user.getNickName());
	   	userInfo.put("remark", friend.getRemark());
	   	userInfo.put("signature", user.getSignature());
	   	userInfo.put("coverImg", user.getCoverImg());
		
	   	HashMap statistic = new HashMap();
	   	statistic.put("chihe", grainDao.getCountByCate(userFid, GlobalConstant.CAT_CHIHE, true));
	   	statistic.put("wanle", grainDao.getCountByCate(userFid, GlobalConstant.CAT_WANLE, true));
	   	statistic.put("other", grainDao.getCountByCate(userFid, GlobalConstant.CAT_OTHER, true));
	   	
	   	rstData.put("user", userInfo);
	   	rstData.put("grainStatistics", statistic);
		
		return Result.success(rstData);
	} 
	
	@RequestMapping(value="/personal/maitian.json", method=RequestMethod.POST)
	public @ResponseBody Object getFriendMaitian(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","fuserId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		List<HashMap> rstData = new ArrayList<HashMap>();
		//step1 查找朋友
		Long userId = jobj.getLong("userId");
		Long userFid = jobj.getLong("fuserId");
		Friend friend = friendDao.findByTwoId(userId, userFid);
		Friend friend2 = friendDao.findByTwoId(userFid, userId);
		if(null == friend || friend.getIsDeleted() || !"1".equals(friend.getFlag()))	return Result.fail(ErrorCode.NOT_FRIEND);
		
		if(null != friend2 && "2".equals(friend2.getFlag())){ //对方已经把你删除
			return Result.success(rstData);  //返回空的麦粒集合
		}
		
		//获取朋友的公开麦粒
		List<HashMap> grains= grainService.getMaitianByUserId(userFid, true);   
		if(null == grains) return Result.fail(ErrorCode.DB_ERROR);
		
		List<Long> gids = new ArrayList<Long>();
		for(HashMap g : grains){
			gids.add((Long)g.get("grainId"));
		}
		
		//获取忽略的麦粒
		List<Long> ignoreIds = grainService.getIgnoreGrainsByIds(gids, userId);
		if(null == ignoreIds) return  Result.fail(ErrorCode.DB_ERROR);
		
		for(HashMap grain : grains){
			Boolean isIgnore = false;
			for(Long ignoreId : ignoreIds){
				if(((Long)grain.get("grainId")).equals(ignoreId)){
					isIgnore = true;
					break;
				}
			}
			
			if(!isIgnore){
				rstData.add(grain);
			}
		}		
		
		return Result.success(rstData);
	}
	
	@RequestMapping(value="/delete.json", method=RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","fuserId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 查找朋友
		Long userId = jobj.getLong("userId");
		Long userFid = jobj.getLong("fuserId");
		Friend friend = friendDao.findByTwoId(userId, jobj.getLong("fuserId"));
		if(null == friend || friend.getIsDeleted() || !"1".equals(friend.getFlag()))	return Result.fail(ErrorCode.NOT_FRIEND);
		
		//step2 删除好友
		HashMap setParams = new HashMap();
		setParams.put("flag", "2");
		HashMap whereParams = new HashMap();
		whereParams.put("user_fid", userFid);
		int exeResult = friendDao.updateByShard(setParams, "friend", "user_id", userId, whereParams);
		
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
}
