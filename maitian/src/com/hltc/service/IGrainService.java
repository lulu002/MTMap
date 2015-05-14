package com.hltc.service;

import java.util.HashMap;

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
	public HashMap praise(String gid, String userId);
	
	/**
	 * 忽略麦粒
	 * @param gid 麦粒id
	 * @param userId 用户id
	 * @return
	 */
	public HashMap ignore(String gid, String userId);
	
	/**
	 * 收藏麦粒
	 * @param gid 麦粒id
	 * @param userId 用户id
	 * @return
	 */
	public HashMap favor(String gid, String userId);
	
	/**
	 * 创建评论
	 * @param jobj
	 * @return
	 */
	public HashMap createComment(JSONObject jobj);
	
	/**
	 * 删除麦粒
	 * @param gid 麦粒id
	 * @return
	 */
	public HashMap deleteGrain(String gid);
}
