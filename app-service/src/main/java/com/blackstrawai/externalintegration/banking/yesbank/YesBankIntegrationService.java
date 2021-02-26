package com.blackstrawai.externalintegration.banking.yesbank;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.YesBankNewAccountSetupVo;


@Service
public class YesBankIntegrationService {


  private final YesBankIntegrationDao yesBankIntegrationDao;

  public YesBankIntegrationService(YesBankIntegrationDao yesBankIntegrationDao) {
    this.yesBankIntegrationDao = yesBankIntegrationDao;
  }

  public int requestAPIBanking(YesBankCustomerInformationVo yesBankCustomerInfoVo)
      throws ApplicationException {
      int returnValue = 0;

    try {

      if (Objects.nonNull(yesBankCustomerInfoVo.getCustomerId()) && Objects.nonNull(yesBankCustomerInfoVo.getAccountNo()) && Objects.nonNull(yesBankCustomerInfoVo.getOrgId())) {

       /* String auth =
            yesBankCustomerInfoVo.getUserName() + ":" + yesBankCustomerInfoVo.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
*/
        returnValue = yesBankIntegrationDao.requestAPIBanking(yesBankCustomerInfoVo);
      } 
      return returnValue;
    } catch (Exception ex) {
      throw new ApplicationException(ex.getMessage());
    }
  }
  
  
  public int enableAPIRequest(YesBankCustomerInformationVo yesBankCustomerInfoVo)
	      throws ApplicationException {
	  	int  returnValue = 0;
	    try {

	    	if(Objects.nonNull(yesBankCustomerInfoVo.getAuthKey1()) && Objects.nonNull(yesBankCustomerInfoVo.getAuthKey2()) &&  Objects.nonNull(yesBankCustomerInfoVo.getOrgId())) {
 	        yesBankIntegrationDao.enableAPIBanking(yesBankCustomerInfoVo);
	        returnValue = 1;
	    	}
	    } catch (Exception ex) {
	      throw new ApplicationException(ex.getMessage());
	    }
        return returnValue;
	  }

  
  
  public YesBankCustomerInformationVo getYesBankAccountDetails (Integer id , Integer orgId) throws ApplicationException {
	  return   yesBankIntegrationDao.getYesBankAccountDetails(id, orgId);

  }
  
  
  
  public Map<String , Map<String , String >> getBillsInvoiceDonutChartCalculation (int orgId) throws ApplicationException{
	  return yesBankIntegrationDao.getBillsInvoiceDonutChartCalculation(orgId);
  }

  
  
  public int saveNewAccountSetupInfo(YesBankNewAccountSetupVo yesBankCustomerInfoVo) throws ApplicationException {
      int  returnValue;
      try{
          yesBankIntegrationDao.saveNewAccountSetupInfo(yesBankCustomerInfoVo);
          returnValue = 1;
      }catch (Exception ex){
          throw new ApplicationException(ex.getMessage());
      }
     return returnValue;
  }

public int updateAccountSetting(YesBankCustomerInformationVo yesBankCustomerInfoVo) throws ApplicationException {
	return yesBankIntegrationDao.updateBankAccountSetting(yesBankCustomerInfoVo);
}


}
