package com.hltc.controller;

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
import com.hltc.dao.ICommentDao;
import com.hltc.dao.IGrainDao;
import com.hltc.entity.Comment;
import com.hltc.entity.Grain;
import com.hltc.service.IGrainService;
import com.hltc.service.IUserService;
import static com.hltc.util.SecurityUtil.*;


/**
 * 麦粒模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/grain")
public class GrainController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IGrainService grainService;
	@Autowired
	private IGrainDao grainDao;
	@Autowired
	private ICommentDao commentDao;
	
	/**
	 * 发布麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/publish.json", method=RequestMethod.POST)
	
	public @ResponseBody Object publish(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, new String[]{"user_id","token","mcate_id","site_name","site_address","lon","lat","city_code","is_public","text"}, true, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"site_id","site_phone", "site_type"}, false, String.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"images"}, false, JSONArray.class);
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersScope(jobj, "site_source", true, new String[]{"0","1"});
		if(null == result.get(Result.SUCCESS))	return result;
				
		//step1 登录验证
		String userId = jobj.getString("user_id");
		result = userService.loginByToken(userId, jobj.getString("token"));
		
		if(null == result.get("success")){
			return result;
		}
		return  grainService.publish(jobj);
	}
	
	/**
	 * 首页查询朋友的麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/home_query.json", method=RequestMethod.POST)
	public @ResponseBody Object homeQuery(@RequestBody JSONObject jobj){
		//step0 参数检验
		HashMap<String, Class> map = new HashMap<String, Class>();
		Map result = null;
		map.put("user_id", String.class);
		map.put("token", String.class);
		map.put("mcate_id", String.class);
		map.put("city_code", String.class);
		map.put("lon", String.class);
		map.put("lat", String.class);
		result = parametersValidate(jobj, map);
		if(null == result.get("success")){
			return result;
		}
		
		//step1 登录验证
		String userId = jobj.getString("user_id");
		result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 查询麦粒
		Double lon = null, lat = null, radius = null;
		try {
			lon = Double.valueOf(jobj.getString("lon"));
			lat = Double.valueOf(jobj.getString("lat"));
			Object r = jobj.get("radius");
			radius = null == r ? null: Double.valueOf(r.toString());
		} catch (Exception e) {
			return Result.fail(ErrorCode.PARAMS_ERROR, "parameters eorr: lon,lat and radius should be double string");
			//TODO log exception
		}
	
		List<Grain> grains = grainDao.findFriendsGrain(userId, jobj.getString("mcate_id"), jobj.getString("city_code"), lon, lat, radius);
		
		return Result.success(grains);
	}
	
	/**
	 * 点赞
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/praise.json", method=RequestMethod.POST)
	public @ResponseBody Object praise(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 点赞表创建或者删除记录
		return grainService.praise(jobj.getString("gid"), userId);
	}
	
	/**
	 * 忽略麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/ignore.json", method=RequestMethod.POST)
	public @ResponseBody Object ignore(@RequestBody JSONObject jobj){
		//step0 参数验证
		
		
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 创建忽略记录
		return grainService.ignore(jobj.getString("gid"), userId);
		
	}
	
	/**
	 * 收藏麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/favor.json", method=RequestMethod.POST)
	public @ResponseBody Object favor(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 创建收藏记录
		return grainService.favor(jobj.getString("gid"), userId);
	}
	
	/**
	 * 评论麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/comment.json", method=RequestMethod.POST)
	public @ResponseBody Object comment(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 麦粒是否存在
		Grain grain = grainDao.findById(jobj.getString("gid"));
		if(null == grain){
			return Result.fail(ErrorCode.GRAIN_NOT_EXIST);
		}
		
		return grainService.createComment(jobj);
	}
	
	/**
	 * 删除评论
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/del_comment.json", method=RequestMethod.POST)
	public @ResponseBody Object delComment(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, (String)jobj.get("token"));
		if(null == result.get("success")){
			return result;
		}
	
		//step2 删除评论
		Comment comment = commentDao.findById((String)jobj.get("cid"));
		if(null == comment){
			return Result.fail(ErrorCode.COMMENT_NOT_EXIST);
		}
		
		if(!userId.equals(comment.getUserId())){
			return Result.fail(ErrorCode.COMMENT_DEL_FAILED);
		}
		
		commentDao.delete(comment);
		
		return Result.success();
	}
	
	/**
	 *  麦粒详情
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/detail.json", method=RequestMethod.POST)
	public @ResponseBody Object getDetail(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//TODO
		return null;
	}
	
	/**
	 *  删除麦粒
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/delete.json", method=RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody JSONObject jobj){
		//step1 登录验证
		String userId = jobj.getString("user_id");
		HashMap result = userService.loginByToken(userId, jobj.getString("token"));
		if(null == result.get("success")){
			return result;
		}
		
		//step2 delete
		return grainService.deleteGrain(jobj.getString("gid"));
	}
	public static void main(String[] args) {
		JSONObject o = new JSONObject();
		o.put("23","234.33");
		
		Object ob = o.get("23");
		
		
		System.out.println(Double.valueOf(ob.toString()));
	}
}
