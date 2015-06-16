package com.hltc.dao;

import java.util.Collection;
import java.util.List;


import com.hltc.entity.User;
import com.hltc.entity.Version;

public interface IVersionDao extends GenericDao<Version>{
	public Version findLatesVersion();
}
