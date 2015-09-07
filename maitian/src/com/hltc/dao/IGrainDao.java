package com.hltc.dao;

import java.util.List;

import net.sf.json.JSONObject;

import com.hltc.entity.Grain;

public interface IGrainDao extends GenericDao<Grain>{
	/**
	 * 查找某个用户的朋友发布的麦粒
	 * @param uids   用户ID集合
	 * @param mtcate_id 选择的类型
	 * @param cityCode 城市编码
	 * @param lon 经度
	 * @param lat 纬度
	 * @param radius 查询半径
	 * @return
	 */
	public List findFriendsGrain(List<Long> uids, String mtcate_id, String cityCode, Double lon, Double lat, Double radius);
	
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
	
	/**
	 * 获取游客推荐麦粒
	 * @param vid
	 * @return
	 */
	public List<Grain> getVrecommendGrains(Long vid);
	
	/**
	 * 获取用户推荐麦粒
	 * @param userId
	 * @return
	 */
	public List<Grain> getUrecommendGrains(Long userId);
	
	/**
	 * 获取某个用户某个类别的总数
	 * @param userId 用户id
	 * @param cateExpression 类目的表达式，例如 %00, 01%, 0000等，使用like查询
	 * @param isPublic  是否公开  true false null ，如果传入null, 则不考虑该条件
	 * @return
	 */
	public Integer getCountByCate(Long userId, String cateExpression, Boolean isPublic);
	
}
