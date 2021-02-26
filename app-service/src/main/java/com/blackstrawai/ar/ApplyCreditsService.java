package com.blackstrawai.ar;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.applycredits.ApplyCreditsBasicVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.applycredits.ReceiptsDetailsVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsCustomerDropDownVo;
import com.blackstrawai.ar.dropdowns.ApplyCreditsDropDownVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;

@Service
public class ApplyCreditsService extends BaseService {

	private Logger logger = Logger.getLogger(ApplyCreditsService.class);

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	ApplyCreditsDao applyCreditsDao;

	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;
	
	@Autowired
	private ArInvoiceDao arInvoiceDao;

	@Autowired
	private ReceiptDao receiptDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao ; 
	
	public void createApplyCredits(ApplyCreditsVo applyCreditsVo) throws ApplicationException {
		logger.info("Entry into Method: createApplyCredits");
		try {
			boolean isCreated = applyCreditsDao.createApplyCredits(applyCreditsVo);
			if (isCreated && !applyCreditsVo.getAttachments().isEmpty() && applyCreditsVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_APPLY_CREDIT,
						applyCreditsVo.getId(), applyCreditsVo.getOrganizationId(), applyCreditsVo.getAttachments());
				logger.info("Upload Successful");

			}
			updateInvoiceAndCreditNoteBalances(applyCreditsVo);

			logger.info("Before thread");
			if(applyCreditsVo.getId()!=null  && (CommonConstants.STATUS_AS_ACTIVE.equals(applyCreditsVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(applyCreditsVo.getStatus()) )) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, applyCreditsVo.getId(), applyCreditsVo.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS);
				journalThread.start();
			}
			
			
		} catch (Exception e) {
			applyCreditsDao.deleteApplyCreditsEntries(applyCreditsVo.getId(),applyCreditsVo.getUserId(),applyCreditsVo.getRoleName());
			throw new ApplicationException(e.getMessage());
		}
	}

	public List<ApplyCreditsBasicVo> getAllApplyCreditsOfAnOrganization(int organizationId, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into Method: getAllApplyCreditsOfAnOrganization");
		return applyCreditsDao.getAllApplyCreditsOfAnOrganization(organizationId,userId,roleName);
	}

	public ApplyCreditsVo getApplyCreditsById(int id) throws ApplicationException {
		logger.info("Entry into Method: getApplyCreditsById");
		ApplyCreditsVo applyCreditsVo;
		try {
			applyCreditsVo = applyCreditsDao.getApplyCreditsById(id);
			if (applyCreditsVo != null && applyCreditsVo.getAttachments().size() > 0
					&& applyCreditsVo.getId() != null) {
				attachmentService.encodeAllFiles(applyCreditsVo.getOrganizationId(), applyCreditsVo.getId(),
						AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_APPLY_CREDIT,
						applyCreditsVo.getAttachments());
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
		return applyCreditsVo;
	}

	public void updateApplyCredits(ApplyCreditsVo applyCreditsVo) throws ApplicationException {
		logger.info("Entry into Method: updateApplyCredits");
		try {
			ApplyCreditsVo existingApplyCreditsVo=null;
			if(applyCreditsVo!=null && applyCreditsVo.getId() !=null) {
				existingApplyCreditsVo=applyCreditsDao.getApplyCreditsById(applyCreditsVo.getId());
			}
			
			Boolean isTxnSuccess = applyCreditsDao.updateApplyCredits(applyCreditsVo);
			if (isTxnSuccess && !applyCreditsVo.getAttachments().isEmpty() && applyCreditsVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_APPLY_CREDIT,
						applyCreditsVo.getId(), applyCreditsVo.getOrganizationId(), applyCreditsVo.getAttachments());
				logger.info("Upload Successfull");
			}
			updateInvoiceAndCreditNoteBalances(existingApplyCreditsVo);//Update previous trasaction may be a reversal needed
			updateInvoiceAndCreditNoteBalances(applyCreditsVo);

			logger.info("Before thread");
			if(applyCreditsVo.getId()!=null  && (CommonConstants.STATUS_AS_ACTIVE.equals(applyCreditsVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(applyCreditsVo.getStatus()) )) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, applyCreditsVo.getId(), applyCreditsVo.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_APPLICATION_OF_FUNDS);
				journalThread.start();
			}
			
			
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public ApplyCreditsCustomerDropDownVo getApplyCreditsCustomerDropDownData(int organizationId)
			throws ApplicationException {
		logger.info("Entry into Method: getApplyCreditsCustomerDropDownData");
		return dropDownDao.getApplyCreditsCustomerDropDownData(organizationId);
	}

	public ApplyCreditsDropDownVo getApplyCreditsDropDownData(int organizationId, int customerId,int currencyId)
			throws ApplicationException {
		logger.info("Entry into Method: getApplyCreditsDropDownData");
		return dropDownDao.getApplyCreditsDropDownData(organizationId, customerId,currencyId);
	}
	
	private Boolean updateInvoiceAndCreditNoteBalances(ApplyCreditsVo applyCreditsVo) throws ApplicationException {
		boolean result=false;
		logger.info("Entry into Method: getApplyCreditsCustomerDropDownData");
		//Updating Balances of Invocies of customer
		if (applyCreditsVo != null && applyCreditsVo.getStatus() != null
				&& applyCreditsVo.getOrganizationId() != null && applyCreditsVo.getCustomerId() != null
				&& applyCreditsVo.getCurrencyId() != null
				&& applyCreditsVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
			List<InvoiceDetailsVo> invoicesList=arInvoiceDao.getAllInvoicesOfCustomerCurrency(applyCreditsVo.getOrganizationId(),applyCreditsVo.getCustomerId(), applyCreditsVo.getCurrencyId());
			for(InvoiceDetailsVo invoiceDetailsVo:invoicesList) {
				if(invoiceDetailsVo!=null && invoiceDetailsVo.getId()!=null)
				new ArBalanceUpdateThread(balanceUpdateDao, invoiceDetailsVo.getId(),ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
				
			}
			List<ReceiptsDetailsVo> receipts=receiptDao.getReceiptsByCustomerId(applyCreditsVo.getOrganizationId(),applyCreditsVo.getCustomerId(),applyCreditsVo.getCurrencyId() );
			for(ReceiptsDetailsVo receipt:receipts) {
			new ArBalanceUpdateThread(balanceUpdateDao, receipt.getId(), ArInvoiceConstants.MODULE_TYPE_AR_RECEIPT).start();;
			}
			result=true;
		}
		return result;
	}

}
