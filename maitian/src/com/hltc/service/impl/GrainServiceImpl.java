package com.hltc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IFavoriteDao;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.INeglectDao;
import com.hltc.dao.IPraiseDao;
import com.hltc.dao.ISiteDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.entity.Comment;
import com.hltc.entity.Favorite;
import com.hltc.entity.Friend;
import com.hltc.entity.Grain;
import com.hltc.entity.Neglect;
import com.hltc.entity.Praise;
import com.hltc.entity.PwdHash;
import com.hltc.entity.Site;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.entity.UserPhotoAlbum;
import com.hltc.entity.VerifyCode;
import com.hltc.service.IGrainService;
import com.hltc.service.IPushService;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.CommonUtil;
import com.hltc.util.SecurityUtil;
import com.hltc.util.UUIDUtil;

@Service("grainService")
public class GrainServiceImpl implements IGrainService{
	@Autowired 
	private ISiteDao siteDao;
	@Autowired 
	private IGrainDao grainDao;
	@Autowired 
	private IUserPhotoAlbumDao userPhotoAlbumDao;
	@Autowired
	private IPraiseDao praiseDao;
	@Autowired
	private INeglectDao neglectDao;
	@Autowired
	private IFavoriteDao favoriteDao;
	@Autowired
	private ICommentDao commentDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IFriendDao friendDao;
	@Autowired
	private IPushService pushService;
	
	@Override
	public String generateGrainId(String cityCode) {
		//4个0表示没有获取到城市编码的情况
		if(null == cityCode) cityCode = "0000";  
		return cityCode + "-"+ UUIDUtil.getUUID();
	}

	@Override
	public HashMap publish(JSONObject jobj) {
		Long userId = jobj.getLong("userId");
		//step1 存储site
		Boolean isSiteExist = false;   //site在数据库中是否存在
		String siteId = null;
		String siteSource = jobj.getString("siteSource");
		Double lat = null, lon = null;
		try {
			lat = Double.valueOf(jobj.getString("lat"));
			lon = Double.valueOf(jobj.getString("lon"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return Result.fail(ErrorCode.PARAMS_ERROR, "wrong value of  parameters: lat or lon");
			//TODO　log
		}
		
		if("0".equals(siteSource)){  //用户自定义的地点
			siteId = UUIDUtil.getUUID();
		}else if("1".equals(siteSource)){ //高德的POI
			siteId = (String)jobj.get("siteId");
			if(null == siteId) return Result.fail(ErrorCode.PARAMS_ERROR, "Missing parameters: siteId");
			if(null != siteDao.findById(siteId)){
				isSiteExist = true;
			}
		}
		siteId = siteId.toUpperCase();
		
		if(!isSiteExist){  //site在数据库中不存在
			HashMap setParams = new HashMap();
			setParams.put("site_id", siteId);
			setParams.put("source", siteSource);
			setParams.put("name", jobj.getString("siteName"));
			setParams.put("address", jobj.getString("siteAddress"));
			setParams.put("phone", jobj.get("sitePhone"));
			setParams.put("gtype", jobj.get("siteType"));
			setParams.put("mtype", jobj.get("mcateId"));
			setParams.put("lat", lat);
			setParams.put("lon", lon);
			int exeResult = siteDao.saveBySQL(setParams, "site");
			if(exeResult == -1) return Result.fail(ErrorCode.DB_ERROR, "site save failed.");
		}
		
		//step2 创建麦粒
		Grain grain = new Grain();
		String cityCode = jobj.getString("cityCode");
		Long time = System.currentTimeMillis();
		
		grain.setIsPublic("1".equals(jobj.getString("isPublic")));
		grain.setLat(lat);
		grain.setLon(lon);
		grain.setMcateId(jobj.getString("mcateId"));
		grain.setSiteId(siteId);
		grain.setText(jobj.getString("text"));
		grain.setUserId(userId);
		grain.setCreateTime(time);
		grain.setCityCode(cityCode);
		grain = grainDao.saveOrUpdateWithBack(grain);
		
		//step3 创建相册	
		JSONArray array = (JSONArray)jobj.get("images");
		
		if(null != array){
			List<UserPhotoAlbum> list = new ArrayList<UserPhotoAlbum>();
			for(Object image : array){
				UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
				userPhotoAlbum.setGid(grain.getGid());
				userPhotoAlbum.setPhoto((String)image);
				userPhotoAlbum.setCreateTime(time);
				userPhotoAlbum.setUserId(userId);
				list.add(userPhotoAlbum);
			}
			return userPhotoAlbumDao.batchAdd(list) ?  Result.success() : Result.fail(ErrorCode.DB_ERROR);
		}
	
		return Result.success();
	}

	@Override
	public HashMap praise(Long gid, Long userId) {
		Praise praise = praiseDao.findByGidAndUserId(gid, userId);
		int exeResult = 0;
		Grain grain = grainDao.findById(gid);
		
		if(null == praise){
			HashMap setParams = new HashMap();
			
			setParams.put("gid", gid);
			setParams.put("user_id", userId);
			setParams.put("create_time",System.currentTimeMillis());
			exeResult = praiseDao.saveBySQL(setParams, "praise");
		}else{
			HashMap setParams = new HashMap();
			HashMap whereParams = new HashMap();
			setParams.put("is_deleted", !praise.getIsDeleted());
			setParams.put("create_time", System.currentTimeMillis());
			whereParams.put("praise_id", praise.getPraiseId());
			exeResult = praiseDao.updateByShard(setParams, "praise", "gid", praise.getGid(), whereParams);
		}
		
		if(exeResult != -1 && (null == praise || praise.getIsDeleted())){
			Map<String, String> map = new HashMap<String,String>();
			map.put("pUid", userId+"");   //点赞者的id
			map.put("grainId", gid+"");    //麦粒的id
			try {
				pushService.sendAndroidCustomizedcast(grain.getUserId()+"", "提示", map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}

	@Override
	public HashMap ignore(Long gid, Long userId) {
		Neglect neglect = neglectDao.findByGidAndUserId(gid, userId);
		int exeResult = 0; 
		if(null == neglect){
			HashMap setParams = new HashMap();
			setParams.put("gid", gid);
			setParams.put("user_id", userId);
			setParams.put("create_time", System.currentTimeMillis());
			exeResult = neglectDao.saveBySQL(setParams, "neglect");
		}else{
			HashMap setParams = new HashMap();
			HashMap whereParams = new HashMap();
			setParams.put("is_deleted", !neglect.getIsDeleted());
			setParams.put("create_time", System.currentTimeMillis());
			whereParams.put("neglect_id", neglect.getNeglectId());
			exeResult = neglectDao.updateByShard(setParams, "neglect", "gid", neglect.getGid(), whereParams);
		}
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}

	@Override
	public HashMap favor(Long gid, Long userId) {
		Favorite favorite = favoriteDao.findByGidAndUserId(gid, userId);
		int exeResult = 0;
		if(null == favorite){
			HashMap setParams = new HashMap();
			setParams.put("gid", gid);
			setParams.put("user_id", userId);
			setParams.put("create_time", System.currentTimeMillis());			
			exeResult = favoriteDao.saveBySQL(setParams, "favorite");
		}else{
			HashMap setParams = new HashMap();
			HashMap whereParams = new HashMap();
			setParams.put("is_deleted", !favorite.getIsDeleted());
			setParams.put("create_time", System.currentTimeMillis());
			whereParams.put("favor_id", favorite.getFavorId());
			exeResult = favoriteDao.updateByShard(setParams, "favorite", "user_id", userId, whereParams);
		}
		return exeResult == -1 ? Result.fail(ErrorCode.DB_ERROR) : Result.success();
	}

	@Override
	public HashMap createComment(JSONObject jobj) {
		Comment comment = new Comment();
		Long userId = jobj.getLong("userId"); //评论人
		Long gid = jobj.getLong("gid");
		Grain grain = grainDao.findById(gid);
		if(null == grain) return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		
		comment.setContent(jobj.getString("text"));
		comment.setGid(gid);
		comment.setCreateTime(System.currentTimeMillis());
		comment.setUserId(userId);
		Long tocid = null;
		if(null != jobj.get("tocid")){
			tocid = jobj.getLong("tocid");
		}
		
		if(null != tocid){
			HashMap whereParams = new HashMap();
			whereParams.put("cid", tocid);
			List<Comment> list = commentDao.findByShard("comment", "gid", gid, whereParams);
			if(null == list || list.size() == 0){
				return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
			}
			if(userId.equals(list.get(0).getUserId())){
				return Result.fail(ErrorCode.COMMENT_FAILED, "不能对自己的评论进行评论");
			}
			comment.setTocid(tocid);
		}
		comment = commentDao.saveOrUpdateWithBack(comment);
		if(null == comment) return Result.fail(ErrorCode.COMMENT_FAILED);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("cUid", userId+""); //评论人 
		map.put("commentId", comment.getCid()+"");
		map.put("grainId", gid+"");
		try {
			pushService.sendAndroidCustomizedcast(grain.getUserId()+"", "提示", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap getGrainDetail(Long gid, Long userId)  throws Exception{
		HashMap result = new HashMap();
		//step1 : find grain
		Grain grain = grainDao.findById(gid);
		if(null == grain || grain.getIsDeleted()) return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		
		Friend friend = null;
		Boolean isSelfView =grain.getUserId().equals(userId);   //是否是麦粒发布者自己查看自己的麦粒 
		if(!isSelfView){  //不是查看自己的麦粒, 那只能查看app推荐的麦粒和朋友推荐的麦粒
			friend = friendDao.findByTwoId(userId, grain.getUserId());
			if(null == friend || grain.getRecommend() == 0) return Result.fail(ErrorCode.NOT_FRIEND);
		}
		
		result.put("grainId", grain.getGid());
		result.put("text", grain.getText());
		result.put("createTime", grain.getCreateTime());
		//step2 : find publisher
		User publisher = userDao.findById(grain.getUserId());
		HashMap publisherInfo = new HashMap();
		if(null == publisher){
			return Result.fail(ErrorCode.USER_NOT_EXIST);
		}
		publisherInfo.put("userId", publisher.getUserId());
		publisherInfo.put("portrait", publisher.getPortrait());
		publisherInfo.put("nickName", publisher.getNickName());
		if(null != friend){
			publisherInfo.put("remark", friend.getRemark());
		}
		result.put("publisher", publisherInfo);
		
		//step3 : find site
		Site site = siteDao.findById(grain.getSiteId());
		HashMap siteInfo = new HashMap();
		if(null == site){
			return Result.fail(ErrorCode.DB_ERROR);
		}
		siteInfo.put("siteId", site.getSiteId());
		siteInfo.put("lon", site.getLon());
		siteInfo.put("lat", site.getLat());
		siteInfo.put("name", site.getName());
		siteInfo.put("address", site.getAddress());
		siteInfo.put("phone", site.getPhone());
		result.put("site", siteInfo);
		//step4 : find images
		List<UserPhotoAlbum> upaList = userPhotoAlbumDao.findByShard("user_photo_album", "gid", gid, null);
		List images = new ArrayList();
		if(null != upaList && upaList.size()>0){
			for(UserPhotoAlbum upa : upaList){
				images.add(upa.getPhoto());
			}
			result.put("images",images);
		}
		//step5: find praise
		HashMap whereParams = new HashMap();
		whereParams.put("is_deleted", false);
		List<Praise> praiseList = praiseDao.findByShard("praise", "gid", gid, whereParams);
		
		//step6: find comment
		List<Comment> comList = commentDao.findByShard("comment", "gid", gid, whereParams);
		
		//step7: 将点赞和评论的用户筛选（只筛选自己的朋友）
		List<Long> ids = new ArrayList<Long>();
		Boolean isSelfPraise = false;   //查看者是否点赞
		Boolean isSelfComment = false;  //查看者是否评论
		for(Praise p : praiseList){
			Long pUid = p.getUserId();
			ids.add(pUid);
			if(pUid.equals(userId)){
				isSelfPraise = true;
			}
		}
		
		for(Comment c : comList){
			Long cUid = c.getUserId();
			ids.add(cUid);
			if(cUid.equals(userId)){
				isSelfComment = true;
			}
		}
		
		List<Friend> friends = friendDao.findByUserIds(userId, CommonUtil.getDistinct(ids));
		List<Long> uid = new ArrayList<Long>();
		if(null == friends) return Result.fail(ErrorCode.DB_ERROR);
		for(Friend f : friends){
			uid.add(f.getUserFid());
		}
		uid.add(userId);  //查看者的id
		
		//step8: 获取用户信息
		List<User> users = userDao.findByIds(uid);
		if(null == users) return Result.fail(ErrorCode.DB_ERROR);
		
		//step9: 将用户信息 填入 praise 和 comment中
		List<HashMap> praiseInfo = new ArrayList<HashMap>();
		List<HashMap> comInfo = new ArrayList<HashMap>();
		for(User u : users){
			Long tmpId = u.getUserId();
			for(Friend f : friends){
				if(f.getUserFid().equals(tmpId)){
					for(Praise p : praiseList){
						if(p.getUserId().equals(tmpId)){
							HashMap data = new HashMap();
							data.put("userId", tmpId);
							data.put("nickName", u.getNickName());
							data.put("remark", f.getRemark());
							praiseInfo.add(data);
							break;
						}
					}
					
					for(Comment c : comList){
						if(c.getUserId().equals(tmpId)){
							HashMap data = new HashMap();
							data.put("cid", c.getCid());
							data.put("tocid", c.getTocid());
							data.put("userId", tmpId);
							data.put("nickName", u.getNickName());
							data.put("portrait", u.getPortrait());
							data.put("remark", f.getRemark());
							data.put("text", c.getContent());
							data.put("createTime", c.getCreateTime());
							comInfo.add(data);
						}
					}
					
					break;
				}
			}
		}
		
		User viewUser = null;  //查看麦粒的UserInfo
		for(User u : users){
			if(u.getUserId().equals(userId)){
				viewUser = u;
				break;
			}
		}
		if(isSelfPraise){  //查看者自己点赞
			HashMap data = new HashMap();
			data.put("userId", viewUser.getUserId());
			data.put("nickName", viewUser.getNickName());
			data.put("remark", "");
			praiseInfo.add(data);
		}
		
		if(isSelfComment){   //查看者自己评论
			for(Comment c : comList){
				if(c.getUserId().equals(userId)){
					HashMap data = new HashMap();
					data.put("cid", c.getCid());
					data.put("tocid", c.getTocid());
					data.put("userId", userId);
					data.put("nickName", viewUser.getNickName());
					data.put("portrait", viewUser.getPortrait());
					data.put("remark", "");
					data.put("text", c.getContent());
					data.put("createTime", c.getCreateTime());
					comInfo.add(data);
				}
			}
		}
		
		result.put("praise", praiseInfo);
		result.put("comment", comInfo);
		return Result.success(result);
	}

	@Override
	public List<HashMap> getRecommendGrains(String type, Long id) {
		List<Grain> grains = null;
		if("visitor".equalsIgnoreCase(type)){
			grains = grainDao.getVrecommendGrains(id);
		}else if("user".equalsIgnoreCase(type)){
			grains = grainDao.getUrecommendGrains(id);
		}
		
	    List<Long> uids = new ArrayList<Long>();
	    List<Long> grainIds = new ArrayList<Long>();
	    List<String> siteids = new ArrayList<String>();
	    List<User> users= null;
	    List<Site> sites = null;
	    List<UserPhotoAlbum> images = null;
	    List<HashMap> result = new ArrayList<HashMap>();
	    
		for(Grain grain : grains){
			uids.add(grain.getUserId());
			grainIds.add(grain.getGid());
			siteids.add(grain.getSiteId());
		}
		
		if(null != grains && grains.size() > 0){
			users = userDao.findByIds(uids);
			images = userPhotoAlbumDao.findByGrainIds(grainIds);
			sites = siteDao.findByIds(siteids);
		}
		
		for(Grain grain : grains){
			Long gid = grain.getGid();
			Long uid = grain.getUserId();
			String siteId = grain.getSiteId();
			HashMap data = new HashMap();	
			data.put("grainId", gid);
			data.put("text", grain.getText());
			data.put("userId", uid);
			
			for(User u : users){
				if(u.getUserId().equals(uid)){
					data.put("portrait", u.getPortrait());
					break;
				}
			}
			
			for(Site s : sites){
				if(s.getSiteId().equals(siteId)){
					data.put("site", s);
					break;
				}
			}
			
			for(UserPhotoAlbum image : images){
				if(image.getGid().equals(gid)){
					data.put("image", image.getPhoto());
					break;
				}
			}
			
			result.add(data);
		}
		
		return result;
	}

	@Override
	public List getHomeQuery(Long userId, String mcateId, String cityCode, Double lon, Double lat, Double radius) {
		List<HashMap> rstData = new ArrayList<HashMap>();
		//step1 find friends
		HashMap whereParams = new HashMap();
		whereParams.put("is_deleted", false);
		List<Friend> friends = friendDao.findByShard("friend", "user_id", userId, whereParams);
		if(null == friends || friends.size() == 0) return rstData;

		List<Long> uids = new ArrayList<Long>();
		for(Friend f: friends){
			uids.add(f.getUserFid());
		}
		//step2 find grains
		List<Object[]> grains = grainDao.findFriendsGrain(uids, mcateId, cityCode, lon, lat, radius);
		if(null == grains || grains.size() == 0) return rstData;
		//step3 find site
		List<String> sids = new ArrayList<String>();
		for(Object[] o : grains){
			String siteId = (String)o[4];
			if(!sids.contains(siteId)){
				sids.add(siteId);
			}
		}
		List<Site> sites = siteDao.findByIds(sids);
		if(null == sites || sites.size() == 0) return rstData;
		
		//step4 组合
		for(Object[] o : grains){
			HashMap data = new HashMap();
			data.put("grainId", o[0]);
			data.put("text", o[1]);
			data.put("userId", o[2]);
			data.put("userPortrait", o[3]);
			data.put("nickName", o[5]);
			data.put("cateId", o[6]);
			for(Friend f : friends){
				if(f.getUserFid().equals(Long.valueOf(String.valueOf(o[2])))){
					data.put("remark", f.getRemark());
				}
			}
			for(Site s : sites){
				if(s.getSiteId().equalsIgnoreCase((String)o[4])){
					data.put("site", s);
				}
			}
			rstData.add(data);
		}
		return rstData;
	}
}
