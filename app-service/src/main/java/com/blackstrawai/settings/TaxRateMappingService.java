package com.blackstrawai.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.ChartOfAccountsThread;

@Service
public class TaxRateMappingService extends BaseService {

	@Autowired
	TaxRateMappingDao taxRateDao;
	
	@Autowired
	TaxRateTypeDao taxRateTypeDao; 
	private Logger logger = Logger.getLogger(TaxRateMappingService.class);

	public TaxRateMappingVo createTaxRate(TaxRateMappingVo taxRateVo) throws ApplicationException {
		logger.info("Entry into method:createTaxRate");
		taxRateVo.setInter(getTaxRateType(taxRateVo));
		TaxRateMappingVo taxRateMappingVo=taxRateDao.createTaxRate(taxRateVo);
		//String name=taxRateVo.getName()+"-"+taxRateVo.getId();
		String name=taxRateMappingVo.getName();
		String displayName=taxRateMappingVo.getName();
		ChartOfAccountsThread chartOfAccountsThread=new ChartOfAccountsThread(taxRateDao,taxRateVo.getOrganizationId(),taxRateVo.getUserId(),name,displayName);
		chartOfAccountsThread.start();
		return taxRateMappingVo;
	}

	public List<TaxRateMappingVo> getAllTaxRatesOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllTaxRatesOfAnOrganization");
		List<TaxRateMappingVo> taxRatesList= taxRateDao.getAllTaxRatesOfAnOrganization(organizationId);
		
		return taxRatesList;
	}
	
	public List<TaxRateMappingVo> getAllTaxRatesOfAnOrganizationByUserAndRole(int organizationId,String userId,String roleName)throws ApplicationException {
		logger.info("Entry into method:getAllTaxRatesOfAnOrganization");
		List<TaxRateMappingVo> taxRatesList= taxRateDao.getAllTaxRatesOfAnOrganizationByUserAndRole(organizationId,userId,roleName);
		
		return taxRatesList;
	}
	
	public TaxRateVariantsVo getTaxRateVariantsForOrganization(int OrganizationId) throws ApplicationException {
		logger.info("Entry into method: getTaxRateVariantsForOrganization");
		List<TaxRateMappingVo> taxRatesList= taxRateDao.getAllTaxRatesOfAnOrganization(OrganizationId);
		List<TaxRateMappingVo> intraTaxRates=new ArrayList<TaxRateMappingVo>();
		List<TaxRateMappingVo> interTaxRates=new ArrayList<TaxRateMappingVo>();
		taxRatesList.forEach(taxRate->{
			TaxRateTypeVo taxRateTypeVo = null;
			try {
				taxRateTypeVo = taxRateTypeDao.getTaxRateTypeById(taxRate.getTaxRateTypeId());
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if("true and true".equalsIgnoreCase(taxRateTypeVo.getIsInter())){
			intraTaxRates.add(taxRate);
			interTaxRates.add(taxRate);
		}else if("true".equalsIgnoreCase(taxRateTypeVo.getIsInter())){
			interTaxRates.add(taxRate);
		}else {
			intraTaxRates.add(taxRate);
		}
			
		});
		
		TaxRateVariantsVo taxRateVariantsVo=new TaxRateVariantsVo();
		taxRateVariantsVo.setInterTaxRates(interTaxRates);
		taxRateVariantsVo.setIntraTaxRates(intraTaxRates);
		
		return taxRateVariantsVo;
	}

	public TaxRateMappingVo deleteTaxRate(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into deleteTaxRate");
		return taxRateDao.deleteTaxRate(id, status,userId,roleName);
	}


	public TaxRateMappingVo updateTaxRate(TaxRateMappingVo taxRateVo) throws ApplicationException {
		logger.info("Entry into method:updateTaxRate");
		taxRateVo.setInter(getTaxRateType(taxRateVo));		
		
		return taxRateDao.updateTaxRate(taxRateVo);
	}
	public TaxRateMappingVo getTaxRateMappingById(int id) throws ApplicationException {
		logger.info("Entry into method:getTaxRateMappingById");
		return taxRateDao.getTaxRateMappingById(id);
	}
	
	private boolean getTaxRateType(TaxRateMappingVo taxRateVo) throws ApplicationException {
		boolean result=false;
		TaxRateTypeVo taxRateTypeVo=taxRateTypeDao.getTaxRateTypeById(taxRateVo.getTaxRateTypeId());
		if(null!=taxRateTypeVo && null!=taxRateTypeVo.getType() && null!=taxRateTypeVo.getIsInter()) {
		if("CESS".equalsIgnoreCase(taxRateTypeVo.getType())){
			result=true;
		}else {
			result=Boolean.parseBoolean(taxRateTypeVo.getIsInter());
		}		
		}
		return result;
	}

}
