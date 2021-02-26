package com.blackstrawai.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class TaxGroupService extends BaseService {

	@Autowired
	TaxGroupDao taxRateGroupDao;
	@Autowired
	TaxRateMappingDao taxRateMappingDao;

	private Logger logger = Logger.getLogger(TaxGroupService.class);

	public TaxGroupVo createTaxGroup(TaxGroupVo taxRateGroupVo) throws ApplicationException {
		logger.info("Entry into method:createTaxGroup");
		TaxGroupVo taxGroupVo=taxRateGroupDao.createTaxGroup(taxRateGroupVo);
		return taxGroupVo;
	}
	public List<TaxGroupVo> getAllTaxGroupsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllTaxGroupsOfAnOrganization");
		List<TaxGroupVo> taxGroupList= taxRateGroupDao.getAllTaxGroupsOfAnOrganization(organizationId);
		taxGroupList.forEach(obj->{
			try {
				obj.setTaxRates(getTaxGroupById(obj.getOrganizationId(),obj.getId()).getTaxRates());
			} catch (ApplicationException e) {
				logger.error(e);
			}
				});
		return taxGroupList;
	}
	
	
	public List<TaxGroupVo> getAllTaxGroupsOfAnOrganizationByUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into getAllTaxGroupsOfAnOrganizationByUserAndRole");
		List<TaxGroupVo> taxGroupList= taxRateGroupDao.getAllTaxGroupsOfAnOrganizationByUserAndRole(organizationId,userId,roleName);
		taxGroupList.forEach(obj->{
			try {
				obj.setTaxRates(getTaxGroupById(obj.getOrganizationId(),obj.getId()).getTaxRates());
			} catch (ApplicationException e) {
				logger.error(e);
			}
				});
		return taxGroupList;
	}

	public TaxGroupVo deleteTaxGroup(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteTaxGroup");
		return taxRateGroupDao.deleteTaxGroup(id, status,userId,roleName);
	}


	public TaxGroupVo updateTaxGroup(TaxGroupVo taxRateGroupVo) throws ApplicationException {
		logger.info("Entry into method:updateTaxGroup");
		
		return taxRateGroupDao.updateTaxGroup(taxRateGroupVo);
	}
	public TaxGroupVo getTaxGroupById(int organizationId,int id) throws ApplicationException {
		logger.info("Entry into method:getTaxGroupById");
		TaxGroupVo taxGroupVo=taxRateGroupDao.getTaxGroupById(id);
		//Convert names to Objects
		String taxRatesIncluded=taxGroupVo.getTaxesIncluded();
		List<TaxRateMappingVo> taxRates=new ArrayList<TaxRateMappingVo>();
		if(null!=taxRatesIncluded) {
			String []taxRatenames=taxRatesIncluded.split("\\,");

			for(String taxRateName:taxRatenames) {
				TaxRateMappingVo taxRateVo=taxRateMappingDao.getTaxRateMappingByName(organizationId,taxRateName.trim());
				if(taxRateVo!=null) {
					taxRates.add(taxRateVo);
				}
			}
		}
		taxGroupVo.setTaxRates(taxRates);

		return taxGroupVo;
	}
}
