package com.hltc.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
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


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.GetFederationTokenRequest;
import com.aliyuncs.sts.model.v20150401.GetFederationTokenResponse;
import com.google.gson.JsonArray;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.IPraiseDao;
import com.hltc.dao.ISiteDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.entity.Comment;
import com.hltc.entity.FederationToken;
import com.hltc.entity.Friend;
import com.hltc.entity.Grain;
import com.hltc.entity.Praise;
import com.hltc.entity.Site;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.entity.UserPhotoAlbum;
import com.hltc.exception.StsException;
import com.hltc.service.IFriendService;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.LogUtil;

import static com.hltc.util.SecurityUtil.*;

/**
 * 授权模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/message")
public class MessageController {
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    public static final String STS_POP_API_VERSION = "2015-04-01";
    public static final String STS_API_VERSION = "1";
    private volatile String regionId = REGION_CN_HANGZHOU;
	@Autowired
	private IUserService userService;
	@Autowired
	private IFriendDao friendDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IGrainDao grainDao;
	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private ICommentDao commentDao;
	@Autowired
	private IUserPhotoAlbumDao userPhotoAlbumDao;
	@Autowired
	private IPraiseDao praiseDao;
	
	@RequestMapping(value="/praise.json" , method=RequestMethod.POST)
	public @ResponseBody Object praise(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","praiseId","grainId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		result = userService.loginByToken(jobj.getLong("userId"), jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
		
		Long grainId = jobj.getLong("grainId");
		Long userId = jobj.getLong("userId");
		//step2 获取信息
		//2.1 get praise
		HashMap whereParams = new HashMap();
		whereParams.put("praise_id", jobj.getLong("praiseId"));
		List<Praise> praises = praiseDao.findByShard("praise", "gid", grainId, whereParams);
		if(null == praises || praises.size() == 0) return Result.fail(ErrorCode.PRAISE_NOT_EXIST);
		Praise praise = praises.get(0);
		if(praise.getIsDeleted()) return Result.fail(ErrorCode.PRAISE_NOT_EXIST);
		
		Long pUid = praise.getUserId();  //点赞者id
		Friend friend = friendDao.findByTwoId(userId, pUid);
		
		Grain grain = grainDao.findById(grainId);
		if(null == grain || !grain.getUserId().equals(userId)) return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
				
		User user = userDao.findById(pUid);
		if(null == user) return Result.fail(ErrorCode.USER_NOT_EXIST);
		
		Site site = siteDao.findById(grain.getSiteId());
		if(null == site) return Result.fail(ErrorCode.SITE_NOT_EXIST);
		List<Long> gids = new ArrayList();
		gids.add(grain.getGid());
		List<UserPhotoAlbum> images =  userPhotoAlbumDao.findByGrainIds(gids);
		if(null == images) return Result.fail(ErrorCode.DB_ERROR);
		
		result.clear();
		
		HashMap userData = new HashMap();
		userData.put("userId", user.getUserId());
		userData.put("portrait", user.getPortrait());
		userData.put("nickName", user.getNickName());
		if(null != friend){
			userData.put("remark", friend.getRemark());
		}
		
		
		HashMap grainData = new HashMap();
		grainData.put("grainId", grain.getGid());
		grainData.put("name", site.getName());
		grainData.put("address", site.getAddress());
		grainData.put("text",grain.getText());
		String imageUrl = "";
		if(images.size() > 0){
			imageUrl = images.get(0).getPhoto();
		}
		grainData.put("image", imageUrl);
		
		result.put("user", userData);
		result.put("grain", grainData);
		result.put("createTime", praise.getCreateTime());
		return Result.success(result);
	}
	
	@RequestMapping(value="/comment.json", method = RequestMethod.POST)
	public @ResponseBody Object comment(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"userId","commentId","grainId"}, true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		result = userService.loginByToken(jobj.getLong("userId"), jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step2获取信息
		Long grainId = jobj.getLong("grainId");
		Long userId = jobj.getLong("userId");
		
        //2.1 get comment
		HashMap whereParams = new HashMap();
		whereParams.put("cid", jobj.getLong("commentId"));
		List<Comment> comments = commentDao.findByShard("comment", "gid", grainId, whereParams);
		if(null == comments || comments.size() == 0) return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
		Comment comment = comments.get(0);
		if(comment.getIsDeleted()) return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
		
		Long cUid = comment.getUserId();  //评论者id   
		Friend friend = friendDao.findByTwoId(userId, cUid);
		
		Grain grain = grainDao.findById(grainId);
		if(null == grain || !grain.getUserId().equals(userId)) return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		
		User user = userDao.findById(cUid);
		if(null == user) return Result.fail(ErrorCode.USER_NOT_EXIST);
		
		Site site = siteDao.findById(grain.getSiteId());
		if(null == site) return Result.fail(ErrorCode.SITE_NOT_EXIST);
		
		List<Long> gids = new ArrayList();
		gids.add(grain.getGid());
		List<UserPhotoAlbum> images =  userPhotoAlbumDao.findByGrainIds(gids);
		if(null == images) return Result.fail(ErrorCode.DB_ERROR);
		
		result.clear();
		HashMap userData = new HashMap();
		userData.put("userId", user.getUserId());
		userData.put("portrait", user.getPortrait());
		userData.put("nickName", user.getNickName());
		if(null != friend){
			userData.put("remark", friend.getRemark());
		}
		
		HashMap grainData = new HashMap();
		grainData.put("grainId", grain.getGid());
		grainData.put("name", site.getName());
		grainData.put("address", site.getAddress());
		grainData.put("text", grain.getText());
		String imageUrl = "";
		if(images.size() > 0){
			imageUrl = images.get(0).getPhoto();
		}
		grainData.put("image", imageUrl);
		
		result.put("user", userData);
		result.put("commentTxt", comments.get(0).getContent());
		result.put("grain", grainData);
		result.put("createTime", comment.getCreateTime());
		
		return Result.success(result);
	}
}
