package com.blackstrawai.settings;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.FinanceCommonDao;
@Service
public class CurrencyService extends BaseService {
	
	@Autowired
	CurrencyDao currencyDao;
	
	@Autowired
	FinanceCommonDao financeCommonDao;
	
	private  Logger logger = Logger.getLogger(CurrencyService.class);

	public CurrencyVo createCurrency(CurrencyVo currencyVo)throws ApplicationException{
		logger.info("Entry into method:createCurrency");
		return currencyDao.createCurrency(currencyVo);
		
	}
	
	public CurrencyVo updateCurrency(CurrencyVo currencyVo)throws ApplicationException{
		logger.info("Entry into method:updateCurrency");
		return currencyDao.updateCurrency(currencyVo);
		
	}
	
	
	public CurrencyVo deleteCurrency(int currencyId,String status)throws ApplicationException {	
		logger.info("Entry into method:deleteCurrency");
		return currencyDao.deleteCurrency(currencyId,status);
	}
	
	public List<CurrencyVo> getAllCurrenciesOfAnOrganization(int organizationId,String organizationName)throws ApplicationException{
		logger.info("Entry into method:getAllCurrenciesOfAnOrganization");
		return currencyDao.getAllCurrenciesOfAnOrganization(organizationId,organizationName);
		
	}
	
	public CurrencyVo getCurrency(int currencyId)throws ApplicationException{
		logger.info("Entry into method:getCurrency");
		return currencyDao.getCurrency(currencyId);
		
	}
	
	public BaseCurrencyVo getBaseCurrencyById(int id) throws ApplicationException {
		BaseCurrencyVo baseCurrencyVo=financeCommonDao.getBaseCurrencyById(id);
		return baseCurrencyVo;	
	}

}
