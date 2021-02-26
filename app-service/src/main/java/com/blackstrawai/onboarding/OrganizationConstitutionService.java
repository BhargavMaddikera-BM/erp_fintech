package com.blackstrawai.onboarding;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.onboarding.organization.OrganizationConstitutionVo;
@Service
public class OrganizationConstitutionService extends BaseService {
	
	@Autowired
	FinanceCommonDao financeCommonDao;
	
	private  Logger logger = Logger.getLogger(OrganizationConstitutionService.class);	

	public List<OrganizationConstitutionVo> getBasicOrganizationConstitution()throws ApplicationException{
		logger.info("Entry into method:getBasicOrganizationConstitution");
		return financeCommonDao.getBasicOrganizationConstitution();
		
	}
	

}
