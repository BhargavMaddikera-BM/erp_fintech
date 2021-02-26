package com.blackstrawai.externalintegration.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;

@Service
public class MessagingService extends BaseService {

	@Autowired
	private MessagingDao messagingDao;
	
	@Autowired
	private YesBankIntegrationDao yesBankIntegrationDao;

	public String getYesBankRegisteredMobileNo(int organizationId, int userId, String roleName,String customerId,String accountName) throws ApplicationException {
		return yesBankIntegrationDao.getYesBankRegisteredMobileNo(organizationId, userId, roleName, customerId, accountName);
	}
	
	public void saveOtpRequest(int organizationId, int userId, String roleName,String mobileNo,String reqId,String type,String subType) throws ApplicationException {
		messagingDao.saveOtpRequest(organizationId, userId, roleName, mobileNo, reqId, type, subType);
	}

	public void updateOtp(String mobileNo,String type,String subType) throws ApplicationException {
	messagingDao.updateOtp(mobileNo,type,subType);	
	}

}
