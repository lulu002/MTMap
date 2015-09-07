package com.hltc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.entity.UserPhotoAlbum;

@Repository("userPhotoAlbumDao")
public class UserPhotoAlbumDaoImpl extends GenericHibernateDao<UserPhotoAlbum> implements IUserPhotoAlbumDao{

	@Override
	public Boolean batchAdd(List<UserPhotoAlbum> userPhotoAlbum) {
		Session session = null;
		try{
			session = getSession();
			session.beginTransaction();
			for(UserPhotoAlbum item : userPhotoAlbum){
				session.save(item);
			}
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}finally{
			session.close();
		}
		
		return true;
	}

	@Override
	public List<UserPhotoAlbum> findByGrainIds(List<Long> ids) {
		List<UserPhotoAlbum> images = new ArrayList<UserPhotoAlbum>();
		if(null == ids || ids.size() == 0) return images;

		Session session = null;
		StringBuilder sql = new StringBuilder("select * from user_photo_album where gid in (");
		for(Long id : ids){
			sql.append(id + ",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		try{
			session = getSession();
			images = session.createSQLQuery(sql.toString()).addEntity(UserPhotoAlbum.class).list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return images;
	}
}
