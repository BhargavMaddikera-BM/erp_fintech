package com.blackstrawai.ap;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationBasicVo;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationVo;
import com.blackstrawai.ap.dropdowns.BalanceConfirmationDropDownVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.DropDownDao;

@Service
public class BalanceConfirmationService {

	@Autowired
	BalanceConfirmationDao balanceConfirmationDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	private AttachmentService attachmentService;

	private Logger logger = Logger.getLogger(BalanceConfirmationService.class);

	public void createBalanceConfirmation(BalanceConfirmationVo balanceConfirmationVo) throws ApplicationException {
		logger.info("Entry into Method: createBalanceConfirmation");
		try {
			boolean isCreated = balanceConfirmationDao.createBalanceConfirmation(balanceConfirmationVo);
			if (isCreated && !balanceConfirmationVo.getAttachments().isEmpty()
					&& balanceConfirmationVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,
						balanceConfirmationVo.getId(), balanceConfirmationVo.getOrganizationId(),
						balanceConfirmationVo.getAttachments());
				logger.info("Upload Successful");
			}
		} catch (Exception e) {
			balanceConfirmationDao.deleteBalanceConfirmationEntries(balanceConfirmationVo.getId(),balanceConfirmationVo.getUserId(),balanceConfirmationVo.getRoleName());
			throw new ApplicationException(e.getMessage());

		}
	}

	public List<BalanceConfirmationBasicVo> getAllBalanceConfirmation(Integer organizationId,Integer vendorId)
			throws ApplicationException {
		logger.info("Entry into Method: getAllBalanceConfirmation");
		return balanceConfirmationDao.getAllBalanceConfirmation(organizationId,vendorId);
	}

	public BalanceConfirmationVo getBalanceConfirmationById(Integer id) throws ApplicationException {
		logger.info("Entry into Method: getBalanceConfirmationById");
		BalanceConfirmationVo balanceConfirmationVo;
		try {
			balanceConfirmationVo = balanceConfirmationDao.getBalanceConfirmationById(id);
			if (balanceConfirmationVo != null && balanceConfirmationVo.getAttachments().size() > 0
					&& balanceConfirmationVo.getId() != null) {
				attachmentService.encodeAllFiles(balanceConfirmationVo.getOrganizationId(),
						balanceConfirmationVo.getId(),
						AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,
						balanceConfirmationVo.getAttachments());
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
		return balanceConfirmationVo;

	}

	public void updateBalanceConfirmation(BalanceConfirmationVo balanceConfirmationVo) throws ApplicationException {
		logger.info("Entry into Method: updateBalanceConfirmation");
		try {
			Boolean isTxnSuccess = balanceConfirmationDao.updateBalanceConfirmation(balanceConfirmationVo);
			if (isTxnSuccess && !balanceConfirmationVo.getAttachments().isEmpty()
					&& balanceConfirmationVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION,
						balanceConfirmationVo.getId(), balanceConfirmationVo.getOrganizationId(),
						balanceConfirmationVo.getAttachments());
				logger.info("Upload Successfull");
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public BalanceConfirmationDropDownVo getBalanceConfimrationDropDownData(int organizationId, int id)
			throws ApplicationException {
		logger.info("Entry into Method: getBalanceConfimrationDropDownData");
		return dropDownDao.getBalanceConfirmationDropDownData(organizationId, id);
	}

	public void statusChange(int id, String status) throws ApplicationException {
		logger.info("Entry into Method: statusChange");
		balanceConfirmationDao.statusChange(id, status);
	}

}
