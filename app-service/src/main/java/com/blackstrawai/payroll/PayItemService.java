package com.blackstrawai.payroll;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.payroll.dropdowns.PayItemDropDownVo;
import com.blackstrawai.settings.BaseCurrencyVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.upload.UploadPayrollVo;

@Service
public class PayItemService extends BaseService{
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PayItemService.class);
	
	@Autowired
	PayItemDao payItemDao;
	
	@Autowired
	DropDownDao dropDownDao;
	
	@Autowired
	OrganizationDao organizationDao;
	
	@Autowired
	FinanceCommonDao financeCommonDao;
	
	@Autowired
	CurrencyDao currencyDao;
	
	
	public PayItemVo createPayItem(PayItemVo data ) throws ApplicationException {
		data=payItemDao.createPayItem(data);
		return data;	
	}
	
	public PayItemVo updatePayItem(PayItemVo data ) throws ApplicationException {
		data=payItemDao.updatePayItem(data);
		return data;	
	}
	
	public PayItemVo deletePayItem(int id,String status,String userId,String roleName) throws ApplicationException {
		return payItemDao.deletePayItem(id,status,userId,roleName);
	}

	public List<PayItemVo> getAllPayItemsOfAnOrganization(int organizationId) throws ApplicationException {
		List<PayItemVo> data=payItemDao.getAllPayItemsOfAnOrganization(organizationId);
		Collections.reverse(data);
		return data;	
	}
	
	
	public List<PayItemVo> getAllPayItemsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		List<PayItemVo> data=payItemDao.getAllPayItemsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(data);
		return data;	
	}
	
	public PayItemVo getPayItemById(int id) throws ApplicationException {
		PayItemVo data=payItemDao.getPayItemById(id);
		return data;	
	}
	
	public PayItemDropDownVo getPayTypeDropDownData(int organizationId) throws ApplicationException {
		PayItemDropDownVo data=dropDownDao.getPayItemDownData(organizationId);
		return data;	
	}
	
	public Map<String,String> processUpload(String roleName,String userId,int organizationId, List<UploadPayrollVo> dataList) throws ApplicationException {
		
		Map<String,String> data=new HashMap<String,String>();
		try
		{
			
			int currency=organizationDao.getOrganizationBaseCurrency(organizationId);
			BaseCurrencyVo baseCurrencyVo=financeCommonDao.getBaseCurrencyById(currency);
			List<CurrencyVo>  currencyList=currencyDao.getAllCurrenciesOfAnOrganization(organizationId, null);
			for(int i=0;i<currencyList.size();i++){
				CurrencyVo currencyVo=currencyList.get(i);
				if(currencyVo.getName().equals(baseCurrencyVo.getName())){
					currency=currencyVo.getId();
					break;
				}
			}
			payItemDao.uploadPayrollData(currency,roleName,userId,organizationId,dataList);
			data.put("Success", "Success");
		}catch(Exception e){
			data.put("Failure", e.getMessage());
		}
		return data;
		
	}
}


/*while(it.hasNext()){
Map.Entry entry=(Map.Entry)it.next();
String key=(String) entry.getKey();
if(key.equals("Salary payable")|| key.equals("Salary Payable")){
	logger.info("Salary Payable");
}else{
	String type=payItemDao.getDebitOrCredit(organizationId,key);
	if(type!=null){
		data.put(key,type);
	}else{
		throw new ApplicationException("Invalid Pay Name");
	}
}		

}*/
