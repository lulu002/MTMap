package com.hltc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hltc.common.Result;
import com.hltc.dao.IFriendDao;
import com.hltc.dao.IUserDao;
import com.hltc.entity.Friend;
import com.hltc.entity.User;
import com.hltc.service.IFriendService;
import com.hltc.util.UUIDUtil;

@Service("friendService")
public class FriendServiceImpl implements IFriendService{
	@Autowired
	private IFriendDao friendDao;
	@Autowired
	private IUserDao userDao;
	
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

	@Override
	public List<HashMap> getAddingFriends(Long userId) {
		List<HashMap> rsltData = new ArrayList<HashMap>();
		List<Friend> friends = friendDao.findAddingByUserId(userId);
		List<Long> uids = new ArrayList<Long>();
		for(Friend f : friends){
			Long uid = f.getUserId();
			if(uid.equals(userId)){
				uids.add(f.getUserFid());
			}else{
				uids.add(uid);
			}
		}
		List<User> users = userDao.findByIds(uids);
		
		if(users == null || users.size() == 0) return rsltData;
		
		for(Friend f : friends){
			Long uid = f.getUserId();
			Long fid = f.getUserFid();
			Boolean isMeAdd = userId.equals(uid);  //是否是我发送的好友请求

			HashMap data = new HashMap();
			for(User u : users){
				Long tuid = u.getUserId();
				if(tuid.equals(uid) || tuid.equals(fid)){
					data.put("status", isMeAdd ? "waiting" : "unaccepted");
					data.put("userId", tuid);
					data.put("userPortrait", u.getPortrait());
					data.put("nickName", u.getNickName());
					data.put("text",f.getText());
					rsltData.add(data);
					break;
				}
			}
		}
		return rsltData;
	}
}
