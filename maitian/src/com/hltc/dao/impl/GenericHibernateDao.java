package com.hltc.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.hltc.common.ErrorCode;
import com.hltc.common.Result;
import com.hltc.dao.GenericDao;
import com.hltc.util.HibernateUtil;


public abstract class GenericHibernateDao<T>  implements GenericDao<T>{
	@SuppressWarnings("unused")
	protected Class<T> clazz;
	
	public GenericHibernateDao(){
		//通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
		Type type = this.getClass().getGenericSuperclass();
		//getActualTypeArguments()返回参数数组
		this.clazz = (Class<T>) ((ParameterizedType)type).getActualTypeArguments()[0];
	}
	
	public void save(T entity){
		Session  session = null;
		try{
			 session = getSession();
			 session.beginTransaction();
			 session.save(entity);
			 session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}
	
	public int saveBySQL(Map<String, Object> setParams, String tableName){
		int exeResult = 0;
		Set<String> keys = setParams.keySet();
		Collection<Object> values = setParams.values();
		
		Session session = null;
		StringBuilder sql = new StringBuilder("insert into `" + tableName + "` ( ");
		
		int keySize =  keys.size();
		for(String key : keys){
			sql.append( "`"+key + "` ,"); 
		}
		sql.deleteCharAt(sql.length() -1);
		sql.append(") values ( ");
		for(int i = 0; i < keySize; i++){
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() -1);
		sql.append(") ");
		
		try{
			session = getSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql.toString());
			int i = 0;
			for(Object value : values){
				query.setParameter(i++, value);
			}
			System.out.println(query.getQueryString());
			exeResult = query.executeUpdate();
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			return -1;
		}finally{
			session.close();
		}
		
		return exeResult;
	}
	
	public void update(T entity){
		Session  session = null;
		try{
			 session = getSession();
			 session.beginTransaction();
			 session.update(entity);
			 session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	
	//增加或者修改实体对象
	public void saveOrUpdate(T entity) {
		Session session = getSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(entity);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}
	//增加或者修改实体对象  
	public T saveOrUpdateWithBack(T entity){
		Session session = getSession();
		T t = null;
		try{
			session.beginTransaction();
			t = (T)session.merge(entity);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
			return t;
		}
	}

	//通过id查找实体对象
	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		Session session =  null;
		try{
			session = getSession();
			return (T)session.get(clazz, id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	//通过id集合查找实体对象集合
	public List<T> findByIds(String tableName, String idColumn, List<Long> ids){
		List<T> list = new ArrayList<T>();
		if(null == ids || ids.size() == 0) return list;
		
		Session session = null;
		StringBuilder sql = new StringBuilder();
		
		//方案1
//		int size = ids.size();
//		for(int i = 0 ; i < size; i++){
//			sql.append("select * from " + tableName + " where " + idColumn + " = "  + ids.get(i));
//			if(i < size - 1){
//				sql.append(" union all ");
//			}
//		}
		
//     方案2		
		sql.append("select * from " + tableName + " where " + idColumn + " in (");
		for(Long id : ids){
			sql.append(id+",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");

		try{
			session = getSession();
			list = session.createSQLQuery(sql.toString()).addEntity(clazz).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		return list;
	}
	
	//删除实体对象
	public void delete(T entity) {
		Session session = getSession();
		try{
			session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}

	//通过指定页号和页面大小返回结果集
	@SuppressWarnings("unchecked")
	public List<T> findList(int pageNo, int pageSize) {
		return getSession().createCriteria(clazz).
			setFirstResult((pageNo-1)*pageSize+1).
			setMaxResults(pageNo*pageSize).list();
	}

	//返回所有记录
	public List<T> getAll() {
		return getSession().createCriteria(clazz).list();
	}
	
	//获得表中记录数
	public int getCountOfAll() {
		Integer count = (Integer)getSession().createCriteria(clazz).
			setProjection(Projections.rowCount()).
			uniqueResult();
		if(null == count){
			return 0;
		}else	{
			return count.intValue();
		}
	}
	
	public int updateByShard(Map<String, Object> setParams, String tableName, String shardKey, Object shardValue, Map<String,Object> whereParams){
		int exeResult = 0;
		
		Set<String> setKeys = setParams.keySet();
		Set<String> whereKeys = null;
		Collection<Object> setValues = setParams.values();
		Collection<Object> whereValues = null;
		
		Session session = null;
		StringBuilder sql = new StringBuilder("update `" + tableName + "` set ");
		
		if(null != whereParams){
			whereKeys = whereParams.keySet();
			whereValues = whereParams.values();	
		}
		
		for(String key : setKeys){
			sql.append(key + " = ?,");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" where " + shardKey + " = ?");
		
		if(null != whereKeys){
			for(String key : whereKeys){
				sql.append(" and " + key + "=? ");
			}
		}
		
		
		try{
			session = getSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql.toString());
			int i = 0;
			for(Object value : setValues){
				query.setParameter(i++, value);
			}
			query.setParameter(i++, shardValue);
			
			if(null != whereValues){
				for(Object value : whereValues){
					query.setParameter(i++, value);
				}
			}
			exeResult = query.executeUpdate();
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			return -1;
		}finally{
			session.close();
		}
		
		return exeResult;
	}
	
	public int deleteByShard(String tableName,String shardKey, Object shardValue, Map<String,Object> whereParams){
		int exeResult = 0;
		int paramSize = null == whereParams ? 0 : whereParams.size();
		
		Set<String> keys = null;
		Collection<Object> values = null;
		
		Session session = null;
		StringBuilder sql  = new StringBuilder("delete from `"+ tableName+ "` where " + shardKey + " = ?" );
		
		if(paramSize != 0 ){
			keys = whereParams.keySet();
			values = whereParams.values();
			for(String key : keys){
				sql.append(" and " + key + " = ?");
			}
		}
		
		try{
			session = getSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setParameter(0, shardValue);
			if(paramSize != 0){
				int i = 1;
				for(Object value : values){
					query.setParameter(i++, value);
				}
			}
			exeResult = query.executeUpdate();
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			return -1;
		}finally{
			session.close();
		}
		
		return exeResult;
		
	}
	
	public List<T> findByShard(String tableName, String shardKey, Object shardValue, Map<String,Object> whereParams){
		List<T> result = null;
		Set<String> whereKeys = null;
		Collection<Object> whereValues = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from `" + tableName+"`");
		sql.append(" where " + shardKey + "=?");
		
		if(null != whereParams){
			whereKeys = whereParams.keySet();
			whereValues = whereParams.values();
		}
		
		if(null != whereKeys){
			for(String key : whereKeys){
				sql.append(" and " + key + "=?");
			}
		}
		
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setParameter(0, shardValue);
			if(null != whereValues){
				int i = 1;
				for(Object value : whereValues){
					query.setParameter(i++, value);
				}
			}
			result = (List<T>)query.addEntity(clazz).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return result; 
	}
	
	public List<T> findByShard(String tableName, String shardKey, Object shardValue, Map<String,Object> whereParams, int start, int length){
		List<T> result = null;
		Set<String> whereKeys = null;
		Collection<Object> whereValues = null;
		Session session = null;
		StringBuilder sql = new StringBuilder("select * from `" + tableName+"`");
		sql.append(" where " + shardKey + "=?");
		
		if(null != whereParams){
			whereKeys = whereParams.keySet();
			whereValues = whereParams.values();
		}
		
		if(null != whereKeys){
			for(String key : whereKeys){
				sql.append(" and " + key + "=?");
			}
		}
		
		try{
			session = getSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			query.setParameter(0, shardValue);
			if(null != whereValues){
				int i = 1;
				for(Object value : whereValues){
					query.setParameter(i++, value);
				}
			}
			result = (List<T>)query.addEntity(clazz).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return result; 
	}
	
	//获取一个Session实例
	public Session getSession(){
		return HibernateUtil.openSession();
	}
	
	/**
	 * 获取当前session
	 * @return
	 */
	public Session getCurrentSession(){
		return HibernateUtil.getCurrentSession();
	}
	
}
