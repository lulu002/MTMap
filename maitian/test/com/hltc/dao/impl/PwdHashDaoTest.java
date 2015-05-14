package com.hltc.dao.impl;



import junit.framework.TestCase;

import org.codehaus.jackson.map.Module.SetupContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.hltc.entity.PwdHash;
import com.hltc.entity.User;

public class PwdHashDaoTest extends TestCase{
	private PwdHashDaoImpl pwdHashDao;
	
	@Override
	protected void setUp() throws Exception {
		pwdHashDao = new PwdHashDaoImpl();
	}
	
	@Test
	public void testFindById(){
		PwdHash ph = pwdHashDao.findById("15527202015042317134899530400017");
		Assert.isTrue(null != ph);
	}

}
