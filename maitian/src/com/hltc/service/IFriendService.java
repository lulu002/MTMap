package com.hltc.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hltc.entity.Token;
import com.hltc.entity.User;

/**
 *  朋友模块Service
 * @author Hengbin Chen
  */
public interface IFriendService {
	
	/**
	 * userIdA 和 userIdB相互添加为好友 
	 * @param userIdA
	 * @param userIdB
	 * @return
	 */
	public HashMap addFriend(Long userIdA, Long userIdB);
	
	/**
	 * userIdA 和 userIdBatch里的所有好友互为朋友
	 * @param userIdA
	 * @param userIdBatch
	 * @return
	 */
	public HashMap addFriendBatch(Long userIdA, List<Long> userIdBatch);
}
