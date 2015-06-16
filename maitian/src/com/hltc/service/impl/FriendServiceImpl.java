package com.hltc.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hltc.common.Result;
import com.hltc.dao.IFriendDao;
import com.hltc.entity.Friend;
import com.hltc.service.IFriendService;
import com.hltc.util.UUIDUtil;

@Service("friendService")
public class FriendServiceImpl implements IFriendService{
	@Autowired
	private IFriendDao friendDao;
	
	@Override
	public HashMap addFriend(Long userIdA, Long userIdB) {
		// TODO Auto-generated method stub
		Friend friendA = friendDao.findByTwoId(userIdA, userIdB);
		if(null == friendA){
			friendA = new Friend();
			friendA.setUserId(userIdA);
			friendA.setUserFid(userIdB);
		}
		friendA.setFlag("1");
		
		Friend friendB = friendDao.findByTwoId(userIdB, userIdA);
		if(null == friendB){
			friendB = new Friend();
			friendB.setUserId(userIdB);
			friendB.setUserFid(userIdA);
		}
		friendB.setFlag("1");
		friendDao.saveOrUpdate(friendA);
		friendDao.saveOrUpdate(friendB);
		
		return Result.success();
	}

	@Override
	public HashMap addFriendBatch(Long userIdA, List<Long> userIdBatch) {
		for(Long userIdB: userIdBatch){
			addFriend(userIdA, userIdB);
		}
		return Result.success();
	}
	

}
