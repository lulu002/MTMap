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
import com.hltc.common.Result;
import com.hltc.dao.IFavoriteDao;
import com.hltc.dao.IFeedbackDao;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.ISiteDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.dao.IVersionDao;
import com.hltc.dao.impl.SiteDaoImpl;
import com.hltc.entity.Favorite;
import com.hltc.entity.Friend;
import com.hltc.entity.Grain;
import com.hltc.entity.Site;
import com.hltc.entity.User;
import com.hltc.entity.UserPhotoAlbum;
import com.hltc.entity.Version;
import com.hltc.service.IFriendService;
import com.hltc.service.IPushService;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.CommonUtil;
import com.hltc.util.FirstLetterUtil;
import com.hltc.util.UUIDUtil;


/**
 * 个人中心(我)模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/my")
public class MyController {
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
	@Autowired
	private IGrainDao grainDao;
	@Autowired
	private IFeedbackDao feedbackDao;
	@Autowired
	private IVersionDao versionDao;
	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IUserPhotoAlbumDao userPhotoAlbumDao;
	@Autowired
	private IFavoriteDao favoriteDao;
	
	@RequestMapping(value="/portrait.json", method=RequestMethod.POST)
	public @ResponseBody Object updatePortrait(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","portrait"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		HashMap setParams = new HashMap();
		setParams.put("portrait", jobj.getString("portrait"));
		int exeResult = userDao.updateByShard(setParams, "user", "user_id", jobj.getLong("userId"), null);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	@RequestMapping(value="/signature.json", method=RequestMethod.POST)
	public @ResponseBody Object updateSignature(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","signature"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;		
		
		HashMap setParams = new HashMap();
		setParams.put("signature", jobj.getString("signature"));
		int exeResult = userDao.updateByShard(setParams, "user", "user_id", jobj.getLong("userId"), null);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	@RequestMapping(value="/coverImg.json", method=RequestMethod.POST)
	public @ResponseBody Object updateCoverImg(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","coverImg"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		HashMap setParams = new HashMap();
		setParams.put("cover_img", jobj.getString("coverImg"));
		int exeResult = userDao.updateByShard(setParams, "user", "user_id", jobj.getLong("userId"), null);
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}
	
	@RequestMapping(value="/friends.json", method=RequestMethod.POST)
	public @ResponseBody Object addFriendOnBackYard(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step2 获取数据
		List<HashMap> rstData = new ArrayList<HashMap>();
		HashMap whereParams = new HashMap();
		whereParams.put("is_deleted", false);
		whereParams.put("flag", "1");
		List<Friend> friends = friendDao.findByShard("friend", "user_id", userId, whereParams);
		if(null == friends || friends.size() == 0) return Result.success(rstData);
		List<Long> uids = new ArrayList<Long>();
		for(Friend f : friends){
			uids.add(f.getUserFid());
		}
		List<User> users = userDao.findByIds(uids);
		if(null == users || users.size() == 0) return Result.success(rstData);
		
		for(User u : users){
			Long uid = u.getUserId();
			HashMap data = new HashMap();
			data.put("userId", uid);
			data.put("nickName", u.getNickName());
			data.put("portrait", u.getPortrait());
			for(Friend f : friends){
				if(!f.getUserFid().equals(uid)) continue;
				String remark = f.getRemark();
				String firstCharacter = "";
				if(null == remark || "".equals(remark)){
					firstCharacter = FirstLetterUtil.getPinYinHeadChar(u.getNickName());
				}else{
					firstCharacter = FirstLetterUtil.getPinYinHeadChar(remark);
				}
				data.put("remark", f.getRemark());
				if(!Character.isLetter(firstCharacter.charAt(0))){  //不是以字母开头
					firstCharacter = "#";
				}
				data.put("firstCharacter", firstCharacter);
				break;
			}
			rstData.add(data);
		}
		
		return Result.success(rstData);
	}
	
	@RequestMapping(value="/friends/search.json", method = RequestMethod.POST)
	public @ResponseBody Object searchNewFriend(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","keyword"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		Long userId = jobj.getLong("userId");
		List<Friend> friends = friendDao.findByShard("friend", "user_id", userId, null);
		if(null == friends) return Result.fail(ErrorCode.DB_ERROR);
		
		List<Long> fids = new ArrayList<Long>();
		for(Friend f : friends){
			fids.add(f.getUserFid());
		}
		List<User> users = userDao.searchNewFriend( jobj.getString("keyword"), fids);
		
		return Result.success(users);
	}
	
	@RequestMapping(value="/friends/adding.json", method = RequestMethod.POST)
	public @ResponseBody Object getAddingFriends(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 call service
		List rsltData = friendService.getAddingFriends(jobj.getLong("userId"));
		return Result.success(rsltData);
	}
	
	@RequestMapping(value="/grain_statistic.json", method=RequestMethod.POST)
	public @ResponseBody Object getGrainCount(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		HashMap data = new HashMap();
		data.put("chihe", grainDao.getCountByCate(userId, "01%"));
		data.put("wanle", grainDao.getCountByCate(userId, "02%"));
		data.put("other", grainDao.getCountByCate(userId, "99%"));
		return Result.success(data);
	}
	
	@RequestMapping(value="/maitian.json", method=RequestMethod.POST)
	public @ResponseBody Object getMyGrains(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get(Result.SUCCESS))	return result;
		
		HashMap whereParams = new HashMap();
		whereParams.put("is_deleted", false);
		List<Grain> grains = grainDao.findByShard("grain1", "user_id", userId, whereParams);
		
		if(null == grains) return Result.fail(ErrorCode.DB_ERROR);
		
		List<HashMap> rsltData = new ArrayList<HashMap>();
		if(grains.size() == 0) return Result.success(rsltData);
		
		List<String> siteIds = new ArrayList<String>();
		List<Long> grainIds = new ArrayList<Long>();
		for(Grain grain : grains){
			siteIds.add(grain.getSiteId());
			grainIds.add(grain.getGid());
		}
		
		List<Site> sites = siteDao.findByIds(CommonUtil.getDistinct(siteIds));
		List<UserPhotoAlbum> photos = userPhotoAlbumDao.findByGrainIds(grainIds);
		
		for(Grain grain : grains){
			Long grainId = grain.getGid();
			String siteId = grain.getSiteId();
			HashMap data = new HashMap();
			data.put("grainId", grainId);
			data.put("text", grain.getText());
			data.put("createTime", grain.getCreateTime());
			for(Site site : sites){
				if(site.getSiteId().equalsIgnoreCase(siteId)){
					data.put("siteName", site.getName());
					data.put("address", site.getAddress());
					break;
				}
			}
			
			for(UserPhotoAlbum album : photos){
				if(album.getGid().equals(grainId)){
					data.put("image", album.getPhoto());
					break;
				}
			}
			rsltData.add(data);
		}
		
		return Result.success(rsltData);
	};
	
	@RequestMapping(value="/favourites.json", method = RequestMethod.POST)
	public @ResponseBody Object getFavorites(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		
		//step1 登录验证
		Long userId = jobj.getLong("userId");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if( null == result.get(Result.SUCCESS)) return result;
		
		HashMap whereParams = new HashMap();
		whereParams.put("is_deleted", false);
		List<Favorite> favors = favoriteDao.findByShard("favorite", "user_id", userId, whereParams);
		if(null == favors) return Result.fail(ErrorCode.DB_ERROR);
		
		List<Long> gids = new ArrayList<Long>();   //麦粒id集合
		for(Favorite favor : favors){
			gids.add(favor.getGid());
		}
		
		
		List<Long> uids = new ArrayList<Long>();   //麦粒发布者id集合
		
		return null;
	};
	
	@RequestMapping(value="/settings/update_nickname.json",method=RequestMethod.POST)
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
	
	/**
	 * 设置 -> 修改密码 -> 发送验证码 
	 * @param jobj  userId,token,phone
	 * @return
	 */
	@RequestMapping(value="/settings/password/verify_code.json", method=RequestMethod.POST)
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
	@RequestMapping(value="/settings/password/verify.json", method = RequestMethod.POST)
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
	@RequestMapping(value="/settings/password/reset.json", method = RequestMethod.POST)
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
	
	@RequestMapping(value="/settings/feedback.json", method = RequestMethod.POST)
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
	@RequestMapping(value="/settings/latest_version.json", method = RequestMethod.GET)
	public @ResponseBody Object getLatestVersion(){
		Version version = versionDao.findLatesVersion();
		return null == version? Result.fail(ErrorCode.DB_ERROR) : Result.success(BeanUtil.beanToMap(version));
	}
}
