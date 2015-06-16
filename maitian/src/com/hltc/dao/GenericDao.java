package com.hltc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface GenericDao<T>{
	void save(T entity);
	/**
	 * 通过SQL插入一条数据
	 * @param setParams
	 * @param entityName
	 * @return 返回操作影响的记录数，若为-1则表示操作失败
	 */
	int saveBySQL(Map<String, Object> setParams, String tableName);
	void update(T entity);
	//增或改
	void saveOrUpdate(T entity);
	//增或改
	T saveOrUpdateWithBack(T entity);
	//删
	void delete(T entity);
	//通过ID查询相应的对象
	T findById(Serializable id);
	//获取数据库中所有对象(一条记录就是一个对象)
	List<T> getAll();
	//利用分页的方法获取某一页面的对象
	List<T> findList(int pageNo,int pageSize);
	//获取数据库总记录
	int getCountOfAll();
	
	/**
	 * 通过SQL更新一条分表j记录，其中包含拆分键
	 * @param setParams  set后面的键值对
	 * @param tableName 数据表名
	 * @param shardKey 分表键
	 * @param shardValue 分表键的值
	 * @param whereParams where后面的键值对
	 * @return 返回操作影响的记录数，若为-1则表示操作失败
	 */
	int updateByShard(Map<String, Object> setParams, String tableName, String shardKey, Object shardValue, Map<String,Object> whereParams);
	
	/**
	 * 通过SQL删除一条分表记录，其中包含拆分键
	 * @param tableName
	 * @param shardKey
	 * @param shardValue
	 * @param whereParams
	 * @return 返回操作影响的记录数，若为-1则表示操作失败
	 */
	int deleteByShard(String tableName,String shardKey, Object shardValue, Map<String,Object> whereParams);
	
	/**
	 * 通过SQL查找一条分表记录，其中包含拆分键
	 * @param tableName
	 * @param shardKey
	 * @param shardValue
	 * @param whereParams
	 * @return 返回操作影响的记录性，若null，则表示没有查询出现异常
	 */
	List<T> findByShard(String tableName, String shardKey, Object shardValue, Map<String,Object> whereParams);
}
