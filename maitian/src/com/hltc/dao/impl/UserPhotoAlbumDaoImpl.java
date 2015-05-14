package com.hltc.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hltc.dao.IUserPhotoAlbumDao;
import com.hltc.entity.UserPhotoAlbum;

@Repository("userPhotoAlbumDao")
public class UserPhotoAlbumDaoImpl extends GenericHibernateDao<UserPhotoAlbum> implements IUserPhotoAlbumDao{

	@Override
	public Boolean batchAdd(List<UserPhotoAlbum> userPhotoAlbum) {
		Session session = getSession();
		try{
			session.beginTransaction();
			for(UserPhotoAlbum item : userPhotoAlbum){
				session.saveOrUpdate(item);
			}
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.close();
		}
		
		return true;
	}

}
