package com.hltc.dao.impl;

import org.springframework.stereotype.Repository;

import com.hltc.dao.ICommentDao;
import com.hltc.entity.Comment;

@Repository("commentDao")
public class CommentDaoImpl extends GenericHibernateDao<Comment> implements ICommentDao{


}
