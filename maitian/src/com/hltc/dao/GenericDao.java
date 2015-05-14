package com.hltc.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T>{
	//增或改
	void saveOrUpdate(T entity);
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
}
