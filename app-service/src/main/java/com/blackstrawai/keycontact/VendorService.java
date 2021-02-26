package com.blackstrawai.keycontact;

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
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.export.VendorExportVo;
import com.blackstrawai.keycontact.dropdowns.VendorDropDownVo;
import com.blackstrawai.keycontact.vendor.VendorBasedOnGstVo;
import com.blackstrawai.keycontact.vendor.VendorVo;

@Service
public class VendorService extends BaseService {

	@Autowired
	VendorDao vendorDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	private AttachmentService attachmentService;

	private Logger logger = Logger.getLogger(VendorService.class);

	public void createVendor(VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into createVendor");
		try {	
			boolean isTxnSuccess = vendorDao.createVendor(vendorVo);
			if (isTxnSuccess && !vendorVo.getAttachments().isEmpty()
					&& vendorVo.getVendorGeneralInformation().getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_VENDOR,
						vendorVo.getVendorGeneralInformation().getId(), vendorVo.getOrganizationId(),
						vendorVo.getAttachments());
				logger.info("Upload Successful");
			}
			// String
			// name=vendorVo.getVendorGeneralInformation().getVendorDisplayName()+"-"+vendorVo.getVendorGeneralInformation().getId();
			// String name = vendorVo.getVendorGeneralInformation().getEmail();
			/*String name = "" + vendorVo.getVendorGeneralInformation().getId();
			String displayName = vendorVo.getVendorGeneralInformation().getVendorDisplayName();
			ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(vendorDao,
					vendorVo.getOrganizationId(), vendorVo.getUserId(), name, displayName);
			chartOfAccountsThread.start();*/
			logger.info("Vendor created Successfully in service layer ");
		} catch (ApplicationException e1) {
			throw e1;
		} catch (Exception e) {
			logger.info("Error in Vendor create in service layer ");
			try {
				vendorDao.deleteVendorEntries(vendorVo.getVendorGeneralInformation().getId(),vendorVo.getUserId(),vendorVo.getRoleName());
			} catch (Exception e1) {
				logger.info("Error in Vendor entries delete in service layer ");
				throw new ApplicationException(e);
			}
			throw new ApplicationException(e);
		}
	}

	public VendorVo deleteVendor(int vendorId, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteVendor");
		return vendorDao.deleteVendor(vendorId, status,userId,roleName);
	}

	/*public List<VendorVo> getAllVendorsOfAnOrganization(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getAllVendorsOfAnOrganization");
		return vendorDao.getAllVendorsOfAnOrganization(organizationId);
	}
*/
	public List<VendorVo> getAllVendorsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllVendorsOfAnOrganizationForUserAndRole");
		return vendorDao.getAllVendorsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
	}
	
	
	public VendorVo getVendorByVendorId(int vendorId) throws ApplicationException {
		logger.info("Entry into method: getVendorByVendorId");
		VendorVo vendorVo;
		try {
			vendorVo = vendorDao.getVendorByVendorId(vendorId);
			if (vendorVo != null && vendorVo.getAttachments().size() > 0
					&& vendorVo.getVendorGeneralInformation().getId() != null) {
				attachmentService.encodeAllFiles(vendorVo.getOrganizationId(),
						vendorVo.getVendorGeneralInformation().getId(), AttachmentsConstants.MODULE_TYPE_VENDOR,
						vendorVo.getAttachments());
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return vendorVo;
	}

	public void updateVendor(VendorVo vendorVo) throws ApplicationException {
		logger.info("Entry into method: updateVendor");
		if (vendorVo.getVendorGeneralInformation().getId() != null) {
			try {
				boolean isTxnSuccess = vendorDao.updateVendor(vendorVo);
				if (isTxnSuccess && !vendorVo.getAttachments().isEmpty()
						&& vendorVo.getVendorGeneralInformation().getId() != null) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_VENDOR,
							vendorVo.getVendorGeneralInformation().getId(), vendorVo.getOrganizationId(),
							vendorVo.getAttachments());
					logger.info("Upload Successfull");
				}
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
		}
	}

	
	public void updateVendorAddress(VendorBasedOnGstVo vendorBasedOnGstVo ,Integer orgId, Integer vendorId , String userId , String roleName) throws ApplicationException {
		logger.info("Entry into method: updateVendorAddress");
		if (orgId != null && vendorId!=null) {
			try {
				boolean isTxnSuccess = vendorDao.updateVendorAddressesBasedOnGst(vendorBasedOnGstVo , orgId,vendorId,userId,roleName);
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
		}
	}

	public VendorDropDownVo getVendorDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorDropDownData");
		return dropDownDao.getVendorDropDownData(organizationId);
	}

	public Map<String, String> processUpload(VendorVo vendorVo, String billingAddressCountry,
			String billingAddressState, String originAddressState, String originAddressCountry, String currency,
			String paymentTerms, String tds, Boolean duplicay) throws ApplicationException {
		logger.info("Entry into method: processUpload");
		Map<String, String> map = new HashMap<String, String>();
		try {
			String status=vendorDao.processUpload(vendorVo, billingAddressCountry, billingAddressState, originAddressState,
					originAddressCountry, currency, paymentTerms, tds, duplicay);
			map.put("Success", "Success");
			if(status!=null && status.equals("Create")){
				String name = "" + vendorVo.getVendorGeneralInformation().getId();
				String displayName = vendorVo.getVendorGeneralInformation().getVendorDisplayName();
				ChartOfAccountsThread chartOfAccountsThread = new ChartOfAccountsThread(vendorDao,
						vendorVo.getOrganizationId(), vendorVo.getUserId(), name, displayName);
				chartOfAccountsThread.start();
			}
			
			return map;
		} catch (Exception e) {
			map.put("Failure", e.getMessage());
			return map;
		}

	}

	public List<VendorExportVo> getListVendorsById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into method: getListVendorsById");
		List<VendorExportVo> vendors = vendorDao.getListVendorsById(exportVo);
		return vendors;

	}
}
