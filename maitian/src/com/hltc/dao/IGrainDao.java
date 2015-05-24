package com.hltc.dao;

import java.util.List;

import net.sf.json.JSONObject;

import com.hltc.entity.Grain;

public interface IGrainDao extends GenericDao<Grain>{
	/**
	 * 查找某个用户的朋友发布的麦粒
	 * @param userId   用户ID
	 * @param mtcate_id 选择的类型
	 * @param cityCode 城市编码
	 * @param lon 经度
	 * @param lat 纬度
	 * @param radius 查询半径
	 * @return
	 */
	public List findFriendsGrain(String userId, String mtcate_id, String cityCode, Double lon, Double lat, Double radius);

	
	/**
	 * 通过查询条件查询麦粒
	 * @param jobj
	 * @return
	 */
	public List queryByCondition(JSONObject jobj);
	
	/**
	 * 通过查询条件查询麦粒总数
	 * @param jobj
	 * @return
	 */
	public Integer countByCondition(JSONObject jobj);
}
