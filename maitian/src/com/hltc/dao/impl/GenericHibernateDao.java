package com.hltc.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.hltc.dao.GenericDao;
import com.hltc.util.HibernateUtil;


public abstract class GenericHibernateDao<T>  implements GenericDao<T>{
	
	@SuppressWarnings("unused")
	private Class<T> clazz;
	
	public GenericHibernateDao(){
		//通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
		Type type = this.getClass().getGenericSuperclass();
		//getActualTypeArguments()返回参数数组
		this.clazz = (Class<T>) ((ParameterizedType)type).getActualTypeArguments()[0];
	}
	
	//增加或者修改实体对象
	public void saveOrUpdate(T entity) {
		Session session = getSession();
		try{
			session.beginTransaction();
			session.merge(entity);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
	}

	//通过id查找实体对象
	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		return (T)getSession().get(clazz, id);
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
