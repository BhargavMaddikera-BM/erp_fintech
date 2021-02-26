package com.blackstrawai.ap;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.creditnote.ApplyFundVo;
import com.blackstrawai.ap.creditnote.VendorCreditCreateVo;
import com.blackstrawai.ap.dropdowns.VendorCreditNoteDropDownVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.DropDownDao;

@Service
public class VendorCreditService {

	private Logger logger = Logger.getLogger(VendorCreditService.class);

	@Autowired
	private BillsInvoiceDao billsInvoiceDao;

	@Autowired
	private VendorCreditDao vendorCreditDao;

	@Autowired
	private DropDownDao dropDownDao;

	@Autowired
	private AttachmentService attachmentService;

	public void applyFunds(List<ApplyFundVo> applyFundVos, int vendorCreditId)
			throws ApplicationException, SQLException {
		logger.info("Entry into method: applyFunds");
		billsInvoiceDao.getTotalAmountOfQuickInvoiceByInvoiceId(applyFundVos, vendorCreditId);

		// TODO: General ledger needs to set once @Manohar give excel sheet with details

	}

	public void createVendorCreditNote(VendorCreditCreateVo vendorCreditCreateVo) throws ApplicationException {
		logger.info("Entry into method: createVendorCreditNote");
		vendorCreditDao.createVendorCreditNote(vendorCreditCreateVo);
		uploadAttachmentsForVendorCredit(vendorCreditCreateVo);
	}

	public void updateVendorCreditNote(VendorCreditCreateVo vendorCreditCreateVo) throws ApplicationException {
		logger.info("Entry into method: updateVendorCreditNote");
		vendorCreditDao.updateVendorCreditNote(vendorCreditCreateVo);
		uploadAttachmentsForVendorCredit(vendorCreditCreateVo);
	}

	public VendorCreditCreateVo getVendorCreditById(Integer creditNoteId) throws ApplicationException {
		logger.info("Entry into method: getVendorCreditById");
		return vendorCreditDao.getVendorCreditNoteId(creditNoteId);
	}

	/**
	 * Upload attachments for vendor credit.
	 *
	 * @param vendorCreditCreateVo the vendor credit create vo
	 */
	private void uploadAttachmentsForVendorCredit(VendorCreditCreateVo vendorCreditCreateVo) {
		if (vendorCreditCreateVo.getId() != null && !CollectionUtils.isEmpty(vendorCreditCreateVo.getAttachments())) {
			logger.info("Entry into uploadAttachmentsForVendorCredit");
			String mouduleType = AttachmentsConstants.MODULE_TYPE_VENDOR_CREDIT;
			attachmentService.upload(mouduleType, vendorCreditCreateVo.getId(),
					vendorCreditCreateVo.getOrganizationId(), vendorCreditCreateVo.getAttachments());
		}
	}

	/**
	 * Gets the vendor credit note drop down data.
	 *
	 * @param organizationId the organization id
	 * @return the vendor credit note drop down data
	 * @throws ApplicationException the application exception
	 */
	public VendorCreditNoteDropDownVo getVendorCreditNoteDropDownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getVendorCreditNoteDropDownData");
		return dropDownDao.getVendorCreditNoteDropDownData(organizationId);
	}

}
