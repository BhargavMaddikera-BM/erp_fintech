package com.blackstrawai.settings;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;

@Service
public class PaymentTermsService extends BaseService {

	@Autowired
	PaymentTermsDao paymentTermsDao;

	private Logger logger = Logger.getLogger(PaymentTermsService.class);

	public PaymentTermsVo createPaymentTerms(PaymentTermsVo paymentTermsVo) throws ApplicationException {
		logger.info("Entry into method:createPaymentTerms");
		return paymentTermsDao.createPaymentTerms(paymentTermsVo);
	}

	/*public List<PaymentTermsVo> getAllPaymentTermsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getAllPaymentTermsOfAnOrganization");
		List<PaymentTermsVo> paymentTermsList= paymentTermsDao.getAllPaymentTermsOfAnOrganization(organizationId);
		Collections.reverse(paymentTermsList);
		return paymentTermsList;
	}*/
	
	public List<PaymentTermsVo> getAllPaymentTermsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllPaymentTermsOfAnOrganizationForUserAndRole");
		List<PaymentTermsVo> paymentTermsList= paymentTermsDao.getAllPaymentTermsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(paymentTermsList);
		return paymentTermsList;
	}


	public PaymentTermsVo deletePaymentTerms(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteVendor");
		return paymentTermsDao.deletePaymentTerms(id,status,userId,roleName);
	}

	public PaymentTermsVo getPaymentTermsById(int id) throws ApplicationException {
		logger.info("Entry into method:getPaymentTermsById");
		return paymentTermsDao.getPaymentTermsById(id);
	}

	public PaymentTermsVo updatePaymentTerms(PaymentTermsVo paymentTermsVo) throws ApplicationException {
		logger.info("Entry into method:updatePaymentTerms");
		return paymentTermsDao.updatePaymentTerms(paymentTermsVo);
	}

}
