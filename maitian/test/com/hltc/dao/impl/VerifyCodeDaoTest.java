package com.hltc.dao.impl;



import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.util.Assert;
import com.hltc.entity.User;
import com.hltc.entity.VerifyCode;

public class VerifyCodeDaoTest extends TestCase{
	private VerifyCodeDaoImpl verifyCodeDao;
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		verifyCodeDao = new VerifyCodeDaoImpl();
	}
	
	@Test
	public void testFindByPhone(){
		
		VerifyCode verifyCode = verifyCodeDao.findByPhone("15172458863");
		
		Assert.isTrue(null != verifyCode);
	}
}
