package com.hltc.service;

import java.util.HashMap;
import java.util.List;

import com.hltc.entity.Grain;

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
	
	/**
	 * 查找userId的所有麦粒
	 * @param userId
	 * @param isPublic  若传入为null，则不筛选该条件
	 * @return 若数据库查询错误，返回null
	 */
	public List<Grain> getGranisByUserId(Long userId, Boolean isPublic);
	
	/**
	 * 查找userId的麦田
	 * @param userId
	 * @param isPublic
	 * @return 若数据库查询错误，返回null
	 */
	public List<HashMap> getMaitianByUserId(Long userId, Boolean isPublic);
	
	/**
	 * 根据传入的麦粒ID集合，获得忽略麦粒的ID集合
	 * @param userId
	 * @param gids
	 * @return 若异常，则返回null,否则返回list结果集
	 */
	public List<Long> getIgnoreGrainsByIds(List<Long> gids, Long userId);
}
