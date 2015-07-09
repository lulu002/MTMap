package com.hltc.service;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

/**
 *  麦粒模块Service
 * @author Hengbin Chen
  */
public interface IGrainService {
	public String generateGrainId(String cityCode); 
	
	/**
	 * 发布麦粒
	 * @return
	 */
	public HashMap publish(JSONObject jobj);
	
	/**
	 * 点赞
	 * @param gid 麦粒id
	 * @param userId 点赞的用户id
	 * @return
	 */
	public HashMap praise(Long gid, Long userId);
	
	/**
	 * 忽略麦粒
	 * @param gid 麦粒id
	 * @param userId 用户id
	 * @return
	 */
	public HashMap ignore(Long gid, Long userId);
	
	/**
	 * 收藏麦粒
	 * @param gid 麦粒id
	 * @param userId 用户id
	 * @return
	 */
	public HashMap favor(Long gid, Long userId);
	
	/**
	 * 创建评论
	 * @param jobj
	 * @return
	 */
	public HashMap createComment(JSONObject jobj);
	
	/**
	 * 通过userId和gid获取麦粒详情
	 * @param gid
	 * @param userId   查看麦粒的用户的ID
	 * @return
	 * @throws Exception 
	 */
	public HashMap getGrainDetail(Long gid, Long userId) throws Exception;
	
	/**
	 * 获取推荐麦粒
	 * @param type 类型 visitor or user
	 * @param id
	 * @return
	 */
	public List<HashMap> getRecommendGrains(String type, Long id);
	
	/**
	 * 首页查询麦粒
	 * @param userId
	 * @param mcateId
	 * @param cityCode
	 * @param lon
	 * @param lat
	 * @param radius
	 * @return
	 */
	public List getHomeQuery(Long userId, String mcateId, String cityCode, Double lon, Double lat, Double radius);
	
}
