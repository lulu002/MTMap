package com.hltc.dao.impl;

import org.springframework.stereotype.Repository;
import com.hltc.dao.IFeedbackDao;
import com.hltc.entity.Feedback;

@Repository("feedbackDao")
public class FeedbackDaoImpl extends GenericHibernateDao<Feedback> implements IFeedbackDao{
}
