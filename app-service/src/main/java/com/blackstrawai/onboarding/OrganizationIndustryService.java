package com.blackstrawai.onboarding;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.onboarding.organization.OrganizationIndustryVo;
@Service
public class OrganizationIndustryService extends BaseService {
	
	@Autowired
	OrganizationIndustryDao organizationIndustryDao;
	
	private  Logger logger = Logger.getLogger(OrganizationIndustryService.class);	

	public List<OrganizationIndustryVo> getBasicOrganizationIndustry()throws ApplicationException{
		logger.info("Entry into method:getBasicOrganizationIndustry");
		return null;
		
	}

}
