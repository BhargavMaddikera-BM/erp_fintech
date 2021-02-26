package com.blackstrawai.keycontact;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.ChartOfAccountsThread;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.export.CustomerExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.keycontact.dropdowns.CustomerDropdownVo;

@Service
public class CustomerService extends BaseService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private DropDownDao dropDownDao;

	private Logger logger = Logger.getLogger(CustomerService.class);

	public void createCustomer(CustomerVo customerVo) throws ApplicationException {
		logger.info("Entry into createCustomer");
		try {
			boolean isTxnSuccess = customerDao.createCustomer(customerVo);
			if (isTxnSuccess && !customerVo.getAttachments().isEmpty() && customerVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_CUSTOMER, customerVo.getId(),
						customerVo.getOrganizationId(), customerVo.getAttachments());
				logger.info("Upload Successful");
			}
			// String
			// name=customerVo.getPrimaryInfo().getCustomerDisplayName()+"-"+customerVo.getId();
/*			String name = customerVo.getPrimaryInfo().getCustomerDisplayName() + "-"
					+ customerVo.getPrimaryInfo().getEmailAddress();
*/			/*String name=""+customerVo.getId();
			String displayName = customerVo.getPrimaryInfo().getCustomerDisplayName();
			ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(customerDao,
					customerVo.getOrganizationId(), customerVo.getUserId(), name, displayName);
			chartOfAccountsThread.start();*/
			logger.info("Customer created Successfully in service layer ");
		} catch (Exception e) {
			logger.info("Error in Customer create in service layer ");
			/*try {
				customerDao.deleteCustomerEntries(customerVo.getId());
			} catch (Exception e1) {
				logger.info("Error in Customer entries delete in service layer ");
				throw new ApplicationException(e);
			}*/
			throw new ApplicationException(e);
		}
	}

	public CustomerVo getCustomerById(Integer customerId) throws ApplicationException {
		logger.info("Entry into getCustomer");
		CustomerVo customerVo;
		try {
			customerVo = customerDao.getCustomerById(customerId);
			if (customerVo != null && customerVo.getAttachments().size() > 0 && customerVo.getId() != null) {
				attachmentService.encodeAllFiles(customerVo.getOrganizationId(), customerVo.getId(),
						AttachmentsConstants.MODULE_TYPE_CUSTOMER, customerVo.getAttachments());
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		logger.info("getCustomerById executed Successfully in service Layer ");
		return customerVo;
	}

	public List<CustomerVo> getAllCustomerListForOrg(Integer orgId) throws ApplicationException {
		logger.info("Entry into getAllCustomerListForOrg");
		return customerDao.getCustomerList(orgId);
	}
	
	
	public List<CustomerVo> getAllCustomerListForUserAndRole(Integer orgId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into getAllCustomerListForUserAndRole");
		return customerDao.getCustomerListForUserAndRole(orgId,userId,roleName);
	}

	public void activateOrDeactivateCustomer(Integer custId, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into activateOrDeactivateCustomer");
		try {
			customerDao.activateOrDeactivateCustomer(custId, status,userId,roleName);
		} catch (Exception e) {
			logger.info("Error in  activateOrDeactivaeCustomer", e);
			throw e;
		}
	}

	public CustomerDropdownVo getCustomerDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getCustomerDropDownData");
		return dropDownDao.getCustomerDropDownData(organizationId);
	}

	public void updateCustomer(CustomerVo customerVo) throws ApplicationException {
		logger.info("Entry into updateCustomer");
		if (customerVo.getId() != null) {
			try {
				boolean isTxnSuccess = customerDao.updateCustomer(customerVo);
				if (isTxnSuccess && !customerVo.getAttachments().isEmpty() && customerVo.getId() != null) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_CUSTOMER, customerVo.getId(),
							customerVo.getOrganizationId(), customerVo.getAttachments());
					logger.info("Upload Successfull");
				}
			} catch (Exception e) {
				logger.info("Error in  updateCustomer", e);
				throw new ApplicationException(e);
			}
		}
	}

	public Map<String, String> processUpload(CustomerVo customerVo, String currency, String billingAddressState,
			String billingAddressCountry, String deliveryAddressState, String deliveryAddressCountry,Boolean duplicacy)
			throws ApplicationException, SQLException {
		logger.info("Entry into processUpload");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String status=customerDao.processUpload(customerVo, currency, billingAddressState, billingAddressCountry,
					deliveryAddressState, deliveryAddressCountry,duplicacy);
			map.put("Success", "Success");
			if(status!=null && status.equals("Create")){
				String name=""+customerVo.getId();
				String displayName = customerVo.getPrimaryInfo().getCustomerDisplayName();
				ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(customerDao,
						customerVo.getOrganizationId(), customerVo.getUserId(), name, displayName);
				chartOfAccountsThread.start();
			}			
			return map;
		} catch (ApplicationException e) {
			map.put("Failure", e.getMessage());
			logger.info("exception inside customer service: " + map.toString());
			return map;
		}
	}

	public List<CustomerExportVo> getListCustomersById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into getListCustomersById");
		List<CustomerExportVo> customers=customerDao.getListCustomersById(exportVo);
		return customers;
		
	}
}
