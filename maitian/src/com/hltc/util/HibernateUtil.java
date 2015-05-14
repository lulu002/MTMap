package com.hltc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	
	static{
		try{
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个Session
	 * @return
	 */
	public static Session openSession(){
		Session session = (Session)threadLocal.get();
		if(session == null || !session.isOpen()){
			session = sessionFactory.openSession();
			threadLocal.set(session);
		}
		return session;
	}
	
	/**
	 * 关闭一个Sesion
	 * @param session
	 */
	public static void closeSession(){
		Session session = (Session)threadLocal.get();
		threadLocal.set(null);
		
		if(null != session){
			session.close();
		}
	}
	
	/**
	 * 获取当前session
	 * @return
	 */
	public static Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 创建所有数据表
	 */
	public static void createTable(){
		new SchemaExport(new Configuration().configure()).create(true, true);
	}
	
	/**
	 * 删除所有表
	 */
	public static void dropTable(){
		new SchemaExport(new Configuration().configure()).drop(true, true);
	}
	
	public static void main(String[] args) {
		createTable();
	}
}
