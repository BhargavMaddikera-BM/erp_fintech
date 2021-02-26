package com.blackstrawai.onboarding;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.onboarding.loginandregistration.ApplicationsVo;


@Service
public class ApplicationService extends BaseService{

	@Autowired
	ApplicationDao applicationDao;
	
	private  Logger logger = Logger.getLogger(ApplicationService.class);
	
	public ApplicationsVo getAllApplicationsAndModules()throws ApplicationException {		
		logger.info("Entry into method:getAllApplicationsAndModules");
		return applicationDao.getAllApplicationsAndModules();
	}
	

}
