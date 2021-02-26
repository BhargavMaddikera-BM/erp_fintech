package com.blackstrawai.onboarding;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.onboarding.organization.OrganizationIndustryVo;
import com.blackstrawai.onboarding.organization.OrganizationTypeVo;
@Service
public class OrganizationTypeService extends BaseService {
	
	@Autowired
	FinanceCommonDao financeCommonDao;
	@Autowired
	OrganizationTypeDao organizationTypeDao;
	
	private  Logger logger = Logger.getLogger(OrganizationTypeService.class);	

	public List<OrganizationTypeVo> getBasicOrganizationType()throws ApplicationException{
		logger.info("Entry into method:getBasicOrganizationType");
		return financeCommonDao.getBasicOrganizationType();
		
	}
	
	public OrganizationTypeVo getOrganizationTypeByName(String name) throws ApplicationException {
		return organizationTypeDao.getOrganizationTypeByName(name);
	}
	public OrganizationIndustryVo getOrganizationDivisionByName(String name) throws ApplicationException {
		return organizationTypeDao.getOrganizationDivisionByName(name);
	}
	

}
