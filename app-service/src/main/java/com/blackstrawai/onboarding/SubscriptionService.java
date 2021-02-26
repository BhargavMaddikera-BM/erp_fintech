package com.blackstrawai.onboarding;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.onboarding.loginandregistration.SubscriptionVo;

@Service
public class SubscriptionService extends BaseService{
	
	@Autowired
	SubscriptionDao subscriptionDao;
	
	private  Logger logger = Logger.getLogger(SubscriptionService.class);
	
	public List<SubscriptionVo> getAllSubscriptions()throws ApplicationException {	
		logger.info("Entry into method:getAllSubscriptions");
		return subscriptionDao.getAllSubscriptions();
	}
	
	
}
