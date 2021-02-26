package com.blackstrawai.ar;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.ApBalanceUpdateDao;
import com.blackstrawai.ap.ApBalanceUpdateThread;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.dropdowns.BasicInvoicesDropdownVo;
import com.blackstrawai.ar.dropdowns.ReceiptBulkDropdownVo;
import com.blackstrawai.ar.dropdowns.ReceiptDropdownVo;
import com.blackstrawai.ar.receipt.ReceiptBulkDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.banking.BankMasterService;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.keycontact.dropdowns.BasicVendorInvoiceDropDownVo;

@Service
public class ReceiptService extends BaseService {

	@Autowired
	ReceiptDao receiptDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	ArInvoiceDao arInvoiceDao;

	@Autowired
	BillsInvoiceDao billsInvoiceDao;
	
	@Autowired
	BankMasterService bankMasterService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;

	@Autowired
	ApBalanceUpdateDao apBalanceUpdateDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	private Logger logger = Logger.getLogger(ReceiptService.class);

	public ReceiptVo createReceipt(ReceiptVo receiptVo) throws ApplicationException {
		logger.info("Entry into method: createReceipt");
		try {
			
			receiptVo = receiptDao.createReceipt(receiptVo);

			updateBalances(receiptVo);
			if (receiptVo != null && receiptVo.getId() != null && receiptVo.getAttachments() != null
					&& !receiptVo.getAttachments().isEmpty()) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS,
						receiptVo.getId(), receiptVo.getOrganizationId(), receiptVo.getAttachments());
				logger.info("Upload Successfull");
			}

			logger.info("Before thread");
			if (receiptVo.getId() != null && (CommonConstants.STATUS_AS_ACTIVE.equals(receiptVo.getStatus())
					|| CommonConstants.STATUS_AS_OPEN.equals(receiptVo.getStatus()))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						receiptVo.getId(), receiptVo.getOrganizationId(), JournalEntriesConstants.SUB_MODULE_RECEIPTS);
				journalThread.start();
			}

			logger.info("Receipt created Successfully in service layer ");
		} catch (Exception e) {
			logger.error("Error in Invoice create in service layer ", e);
			throw new ApplicationException(e.getMessage());
		}
		return receiptVo;
	}

	private void updateBalances(ReceiptVo receiptVo) throws ApplicationException {
		// Updating invoice Balance
		if (receiptVo != null && receiptVo.getStatus() != null 	&& receiptVo.getReceiptType() != null) {

			// Consider only AR Invoices
			if (!receiptVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT) && !receiptVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {
				if(ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS.equals(receiptVo.getReceiptType())) {
					if(receiptVo.getOrganizationId()>0 && receiptVo.getCustomerId()!=null&& receiptVo.getCurrencyId()>0) {
						//Update Invoices of Customer & Currency
						List<InvoiceDetailsVo> invoicesList=arInvoiceDao.getAllInvoicesOfCustomerCurrency(receiptVo.getOrganizationId(),receiptVo.getCustomerId(), receiptVo.getCurrencyId());
						for(InvoiceDetailsVo invoiceDetailsVo:invoicesList) {
							if(invoiceDetailsVo!=null && invoiceDetailsVo.getId()!=null)
								new ArBalanceUpdateThread(balanceUpdateDao, invoiceDetailsVo.getId(),ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();

						}
					}
					for (ReceiptBulkDetailsVo receiptBulkDetailsVo : receiptVo.getReceiptBulkDetails()) {
						Integer referenceId = receiptBulkDetailsVo.getReferenceId();
						String parentType = receiptBulkDetailsVo.getParentType();
						if (parentType != null) {
								
								if (parentType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT)) {
								// Updating Other Receipts
								new ArBalanceUpdateThread(balanceUpdateDao, referenceId,
										ArInvoiceConstants.MODULE_TYPE_AR_RECEIPT).start();
							} else if (parentType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREDIT_NOTE)) {
								// Updating Credit Notes
								new ArBalanceUpdateThread(balanceUpdateDao, referenceId,
										ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE).start();
							}
							logger.info("Invoice Balance updated!!:");
						}
					}
					new ArBalanceUpdateThread(balanceUpdateDao, receiptVo.getId(),
							ArInvoiceConstants.MODULE_TYPE_AR_RECEIPT).start();
				}else if(ReceiptConstants.AR_RECEIPT_TYPE_VENDOR_REFUNDS.equals(receiptVo.getReceiptType())) {					
					if(receiptVo.isRecordVendorCreditDetails()) {
						new ApBalanceUpdateThread(apBalanceUpdateDao, receiptVo.getId(), JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES).start();
					}
					
					
					if(receiptVo.getVendorId()!=null && receiptVo.getCurrencyId()>0) {
						List<Integer> invoices=billsInvoiceDao.getInvoiceIdsForVendor(receiptVo.getVendorId(),receiptVo.getCurrencyId());
						if(invoices!=null && !invoices.isEmpty()) {
							for(Integer invoiceId:invoices) {
								new ApBalanceUpdateThread(apBalanceUpdateDao, invoiceId, JournalEntriesConstants.SUB_MODULE_INVOICE_WITH_BILLS).start();
							}
						}
					}
				}
			}
		}
	}

	public List<ReceiptVo> getAllReceiptsOfAnOrganizationForUserAndRole(int OrganizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method:getAllReceiptsOfAnOrganization");
		List<ReceiptVo> receiptList = receiptDao.getAllReceiptsOfAnOrganizationForUserAndRole(OrganizationId, userId,
				roleName);

		return receiptList;
	}

	public ReceiptVo updateReceipt(ReceiptVo receiptVo) throws ApplicationException {
		logger.info("Entry into method:updateReceipt  ");
		try {
			ReceiptVo existingReceipt=null;
			if(receiptVo!=null && receiptVo.getId()!=null) {
			existingReceipt=receiptDao.getReceiptById(receiptVo.getId());
			}
			receiptVo = receiptDao.updateReceipt(receiptVo);

			updateBalances(existingReceipt);//Update existing Balances(May be a reversal needed if user changes customer/currency)
			updateBalances(receiptVo);

			if (receiptVo != null && receiptVo.getAttachments() != null && !receiptVo.getAttachments().isEmpty()
					&& receiptVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS,
						receiptVo.getId(), receiptVo.getOrganizationId(), receiptVo.getAttachments());
				logger.info("Upload Successfull");
			}

			logger.info("Before thread");
			if (receiptVo.getId() != null && (CommonConstants.STATUS_AS_ACTIVE.equals(receiptVo.getStatus())
					|| CommonConstants.STATUS_AS_OPEN.equals(receiptVo.getStatus()))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						receiptVo.getId(), receiptVo.getOrganizationId(), JournalEntriesConstants.SUB_MODULE_RECEIPTS);
				journalThread.start();
			}

			logger.info("Receipt Updated Successfully in service layer ");
		} catch (Exception e) {
			logger.info("Error in Invoice create in service layer ");
			throw new ApplicationException(e.getMessage());
		}
		return receiptVo;
	}

	public ReceiptVo getReceiptById(int id) throws ApplicationException {
		logger.info("Entry into method:getReceiptById");
		ReceiptVo receiptVo = receiptDao.getReceiptById(id);
		if (receiptVo != null && receiptVo.getAttachments() != null && receiptVo.getAttachments().size() > 0
				&& receiptVo.getId() != null) {
			attachmentService.encodeAllFiles(receiptVo.getOrganizationId(), receiptVo.getId(),
					AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS, receiptVo.getAttachments());
		}
		return receiptVo;
	}

	public ReceiptDropdownVo getReceiptDropdownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method: getReceiptDropdownData");
		return dropDownDao.getReceiptDropDownData(organizationId);
	}

	public BasicInvoicesDropdownVo getReceiptInvoicesForCustomer(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into getReceiptInvoicesForCustomer");
		return dropDownDao.getReceiptInvoicesForCustomer(organizationId, currencyId, customerId, receiptId);
	}

	public List<ReceiptBulkDropdownVo> getBulkReceiptDataForCustomer(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into getBulkReceiptDataForCustomer");
		return dropDownDao.getBulkReceiptDataForCustomer(organizationId, currencyId, customerId, receiptId);
	}

	public BasicVendorInvoiceDropDownVo getReceiptInvoicesForVendor(int organizationId, int currencyId, int vendorId)
			throws ApplicationException {
		logger.info("Entry into getReceiptInvoicesForVendor");
		return dropDownDao.getReceiptInvoicesForVendor(organizationId, currencyId, vendorId);
	}

	public BasicInvoicesDropdownVo getDueBalancesForCustomer(int organizationId, int customerId)
			throws ApplicationException {
		logger.info("Entry into getDueBalancesForCustomer");
		return dropDownDao.getDueBalancesForCustomer(organizationId, customerId);
	}

}
