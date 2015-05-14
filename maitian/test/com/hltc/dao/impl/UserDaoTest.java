package com.hltc.dao.impl;



import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.jackson.map.Module.SetupContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.hltc.entity.User;

public class UserDaoTest extends TestCase{
	private UserDaoImpl userDao;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		 userDao = new UserDaoImpl();
	}
	
	@Test
	public void testFindByPhone(){
		User user  = userDao.findByPhone("13767442857");
		Assert.isTrue(null != user);
	}
	
	@Test
	public void testFindByUniqueInfo(){
		User user = userDao.findByUniqueInfo("15527207898");
		Assert.isTrue(null != user);
	}
	
	@Test
	public void testFindByPhones(){
		List phones = new ArrayList();
		phones.add("15527207898");
		phones.add("13767442857");
		List<User> users = userDao.findByPhones(phones);
		System.out.println("testFindByPhones: users.seize" +users.size());
	}
}
