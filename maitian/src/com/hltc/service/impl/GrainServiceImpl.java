package com.hltc.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloopen.rest.sdk.CCPRestSDK;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IFavoriteDao;
import com.hltc.dao.IGrainDao;
import com.hltc.dao.IIgnoreDao;
import com.hltc.dao.IPraiseDao;
import com.hltc.dao.IPwdHashDao;
import com.hltc.dao.ISiteDao;
import com.hltc.dao.ITokenDao;
import com.hltc.dao.IUserDao;
import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.dao.IVerifyCodeDao;
import com.hltc.entity.Comment;
import com.hltc.entity.Favorite;
import com.hltc.entity.Grain;
import com.hltc.entity.Ignore;
import com.hltc.entity.Praise;
import com.hltc.entity.PwdHash;
import com.hltc.entity.Site;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.entity.UserPhotoAlbum;
import com.hltc.entity.VerifyCode;
import com.hltc.service.IGrainService;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
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
	private IIgnoreDao ignoreDao;
	@Autowired
	private IFavoriteDao favoriteDao;
	@Autowired
	private ICommentDao commentDao;
	
	@Override
	public String generateGrainId(String cityCode) {
		//4个0表示没有获取到城市编码的情况
		if(null == cityCode) cityCode = "0000";  
		return cityCode + "-"+ UUIDUtil.getUUID();
	}

	@Override
	public HashMap publish(JSONObject jobj) {
		String userId = jobj.getString("userId");
		//step1 存储site
		String siteSource = jobj.getString("siteSource");
		Site site = new Site();
		site.setSource(siteSource);
		String siteId = null;
		if("0".equals(siteSource)){  //用户自定义的地点
			siteId = UUIDUtil.getUUID();
		}else if("1".equals(siteSource)){ //高德的POI
			siteId = (String)jobj.get("siteId");
			if(null == siteId) return Result.fail(ErrorCode.PARAMS_ERROR, "Missing parameters: siteId");
		}
		
		Double lat = null, lon = null;
		try {
			lat = Double.valueOf(jobj.getString("lat"));
			lon = Double.valueOf(jobj.getString("lon"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return Result.fail(ErrorCode.PARAMS_ERROR, "wrong value of  parameters: lat or lon");
			//TODO　log
		}
		site.setSiteId(siteId);
		site.setName(jobj.getString("siteName"));
		site.setAddress(jobj.getString("siteAddress"));
		site.setTelphone(jobj.getString("sitePhone"));
		site.setGtype(jobj.getString("siteType"));
		site.setMtype(jobj.getString("mcateId"));
		site.setLat(lat);
		site.setLon(lon);
		siteDao.saveOrUpdate(site);
		
		//step2 创建麦粒
		Grain grain = new Grain();
		String cityCode = jobj.getString("cityCode");
		Long time = System.currentTimeMillis();
		String gid = generateGrainId(cityCode);
		
		grain.setGid(gid);
		grain.setIsPublic(jobj.getString("isPublic"));
		grain.setLat(lat);
		grain.setLon(lon);
		grain.setMcateId(jobj.getString("mcateId"));
		grain.setSiteId(siteId);
		grain.setText(jobj.getString("text"));
		grain.setUserId(userId);
		grain.setTime(time);
		grainDao.saveOrUpdate(grain);
		
		//step3 创建相册	
		JSONArray array = (JSONArray)jobj.get("images");
		
		if(null != array){
			List images = JSONArray.toList(array);
			List<UserPhotoAlbum> list = new ArrayList<UserPhotoAlbum>();
			for(int i = 0; i < images.size(); i++){
				MorphDynaBean image = (MorphDynaBean) images.get(i);
				UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
				userPhotoAlbum.setGid(gid);
				userPhotoAlbum.setPhoto((String)image.get("large"));
				userPhotoAlbum.setPhotoSmall((String)image.get("small"));
				userPhotoAlbum.setTime(time);
				userPhotoAlbum.setUpaId(UUIDUtil.getUUID());
				userPhotoAlbum.setUserId(userId);
				list.add(userPhotoAlbum);
			}
			userPhotoAlbumDao.batchAdd(list);
		}
	
		return Result.success();
	}

	@Override
	public HashMap praise(String gid, String userId) {
		Praise praise = praiseDao.findByGidAndUserId(gid, userId);
		if(null == praise){
			praise = new Praise(UUIDUtil.getUUID());
			praise.setGid(gid);
			praise.setUserId(userId);
			praise.setTime(System.currentTimeMillis());
			praiseDao.saveOrUpdate(praise);
		}else{
			praiseDao.delete(praise);
		}
		return Result.success();
	}

	@Override
	public HashMap ignore(String gid, String userId) {
		Ignore ignore = ignoreDao.findByGidAndUserId(gid, userId);
		if(null == ignore){
			ignore = new Ignore(UUIDUtil.getUUID());
			ignore.setGid(gid);
			ignore.setUserId(userId);
			ignore.setTime(System.currentTimeMillis());
			ignoreDao.saveOrUpdate(ignore);
		}else{
			ignoreDao.delete(ignore);
		}
		return Result.success();
	}

	@Override
	public HashMap favor(String gid, String userId) {
		// TODO Auto-generated method stub
		Favorite favorite = favoriteDao.findByGidAndUserId(gid, userId);
		if(null == favorite){
			favorite = new Favorite(UUIDUtil.getUUID());
			favorite.setGid(gid);
			favorite.setUserId(userId);
			favorite.setTime(System.currentTimeMillis());
			favoriteDao.saveOrUpdate(favorite);
		}else{
			favoriteDao.delete(favorite);
		}
		return Result.success();
	}

	@Override
	public HashMap createComment(JSONObject jobj) {
		String cid = UUIDUtil.getUUID();
		Comment comment = new Comment(cid);
		comment.setContent(jobj.getString("text"));
		comment.setGid(jobj.getString("gid"));
		comment.setTime(System.currentTimeMillis());
		comment.setUserId(jobj.getString("user_id"));
		String  toCid = (String)jobj.get("to_cid");
		if(null != toCid || "".equals(toCid)){
			Comment temp = commentDao.findById(toCid);
			if(null == temp){
				return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
			}
			comment.setTocid(toCid);
		}
		commentDao.saveOrUpdate(comment);
		
		HashMap map = new HashMap();
		map.put("cid", cid);
		return Result.success(map);
	}

	@Override
	public HashMap deleteGrain(String gid) {
		Grain grain = grainDao.findById(gid);
		if(null == grain){
			return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		}
		
		grain.setIsDeleted(true);
		
		grainDao.saveOrUpdate(grain);
		
		return Result.success();
	}
	

}
