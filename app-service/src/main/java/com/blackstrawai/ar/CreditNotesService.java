package com.blackstrawai.ar;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.dropdowns.CreditNoteDropdownVo;
import com.blackstrawai.ar.dropdowns.CustomerAccountsDropdownVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.settings.SettingsModuleOrganizationDao;
import com.blackstrawai.settings.SettingsModuleOrganizationVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowInvoiceVo;
import com.blackstrawai.workflow.WorkflowProcessHelperService;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.WorkflowThread;

@Service
public class CreditNotesService extends BaseService {

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	CreditNotesDao creditNotesDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private ArInvoiceService arInvoiceService;

	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;

	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	@Autowired
	private WorkflowProcessHelperService workflowProcessHelperService;

	@Autowired
	private WorkflowProcessService workflowProcessService;

	@Autowired
	private SettingsModuleOrganizationDao settingsModuleOrganizationDao;


	private Logger logger = Logger.getLogger(CreditNotesService.class);

	public CreditNotesVo createCreditNotes(CreditNotesVo creditNotesVo) throws ApplicationException {
		logger.info("Entry into method:createCreditNotes");
		try {
			creditNotesVo=creditNotesDao.createCreditNotes(creditNotesVo);
			if(creditNotesVo!=null) {
				if(creditNotesVo.getId()>0 && creditNotesVo.getAttachments()!=null && !creditNotesVo.getAttachments().isEmpty()) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE, creditNotesVo.getId(), creditNotesVo.getOrganizationId(), creditNotesVo.getAttachments());
					logger.info("Upload Successfull");
				}
				if(creditNotesVo.getStatus()!=null && creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
					String invoiceId=creditNotesVo.getOriginalInvoiceId();
					int creditNoteId=creditNotesVo.getId();
					//Updating due amount in invoice
					updateDueBalance(invoiceId, creditNoteId);

					startWorkflow(creditNotesVo, WorkflowConstants.WORKFLOW_OPERATION_CREATE);
				}

				logger.info("Before thread");
				if(creditNotesVo.getId()!=0  && (CommonConstants.STATUS_AS_ACTIVE.equals(creditNotesVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(creditNotesVo.getStatus()) )) {
					logger.info("To thread");
					JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, creditNotesVo.getId(), creditNotesVo.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
					journalThread.start();
				}
			}
		}catch (Exception e) {
			logger.error("Error while creating Credit note",e);
			throw new ApplicationException(e);
		}
		return creditNotesVo;
	}
	public List<CreditNotesVo> getAllCreditNotesByFilter(ArInvoiceFilterVo creditNotesFilterVo,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into getAllCreditNotesByFilter");
		List<CreditNotesVo> creditNotesList= creditNotesDao.getAllCreditNotesByFilter(creditNotesFilterVo,userId,roleName);

		return creditNotesList;
	}

	public CreditNotesVo deleteCreditNotes(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteCreditNotes");
		CreditNotesVo creditNotesVo=creditNotesDao.deleteCreditNotes(id, status,userId,roleName);
		if(creditNotesVo!=null && creditNotesVo.getId()>0) {
			int invoiceId=creditNotesDao.getInvoiceForCreditNote(creditNotesVo.getId());
			int creditNoteId=creditNotesVo.getId();
			new ArBalanceUpdateThread(balanceUpdateDao, invoiceId, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
			new ArBalanceUpdateThread(balanceUpdateDao, creditNoteId, ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE).start();;
		}
		return creditNotesVo;
	}


	public CreditNotesVo updateCreditNotes(CreditNotesVo creditNotesVo) throws ApplicationException {
		logger.info("Entry into method:updateCreditNotes");
		int existingCreditNoteId = 0;
		String existingInvoiceId = null;
		if (creditNotesVo != null && creditNotesVo.getId() > 0) {
			WorkflowInvoiceVo workflowInvoiceVo = creditNotesDao
					.getWorkflowRequiredDataForCreditNotebyId(creditNotesVo.getId());
			existingCreditNoteId = workflowInvoiceVo.getId();
			existingInvoiceId = workflowInvoiceVo.getOriginalInvoiceId() != null
					? workflowInvoiceVo.getOriginalInvoiceId() + ""
					: null;
		}
		creditNotesVo = creditNotesDao.updateCreditNotes(creditNotesVo);
		if (creditNotesVo != null) {

			WorkflowInvoiceVo workflowInvoiceVo = creditNotesDao
					.getWorkflowRequiredDataForCreditNotebyId(creditNotesVo.getId());
			String invoiceId = creditNotesVo.getOriginalInvoiceId();
			int creditNoteId = creditNotesVo.getId();
			if (workflowInvoiceVo != null && workflowInvoiceVo.getStatus() != null
					&& workflowInvoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN)
					&& creditNotesVo != null && creditNotesVo.getStatus() != null
					&& creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)) {
				creditNotesVo.setStatus(CommonConstants.STATUS_AS_OPEN);
			}
			if (creditNotesVo.getAttachments() != null && !creditNotesVo.getAttachments().isEmpty()
					&& creditNotesVo.getId() > 0) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE,
						creditNotesVo.getId(), creditNotesVo.getOrganizationId(), creditNotesVo.getAttachments());
				logger.info("Upload Successfull");
			}

			

			// Update Balance
			if (creditNotesVo.getStatus() != null
					&& !creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)
					&& !creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {
				updateDueBalance(existingInvoiceId, existingCreditNoteId);// Update previous if user changes customer/currency need to reverse the transaction
				// Updating due amount in invoice
				updateDueBalance(invoiceId, creditNoteId);

			}
			// Trigger Workflow
			if (creditNotesVo.getStatus() != null
					&& !(creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_VOID))) {

				if (creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)) {
					workflowInvoiceVo = creditNotesDao.getWorkflowRequiredDataForCreditNotebyId(creditNotesVo.getId());
					// If it is not regular draft
					if (workflowInvoiceVo != null && workflowInvoiceVo.getPendingApprovalStatus() != null
							&& (workflowInvoiceVo.getPendingApprovalStatus()
									.equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL)
									|| workflowInvoiceVo.getPendingApprovalStatus()
											.equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_APPROVAL_DENIED))) {
						updateDueBalance(invoiceId, creditNoteId);

						startWorkflow(creditNotesVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);
					}
				} else {
					startWorkflow(creditNotesVo, WorkflowConstants.WORKFLOW_OPERATION_UDATE);
				}

			}
			logger.info("Before thread");
			if (creditNotesVo.getId() != 0 && (CommonConstants.STATUS_AS_ACTIVE.equals(creditNotesVo.getStatus())
					|| CommonConstants.STATUS_AS_OPEN.equals(creditNotesVo.getStatus()))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						creditNotesVo.getId(), creditNotesVo.getOrganizationId(),
						JournalEntriesConstants.SUB_MODULE_CREDIT_NOTES);
				journalThread.start();
			}
		}
		return creditNotesVo;
	}

	private void updateDueBalance(String invoiceId, int creditNoteId) {
		new ArBalanceUpdateThread(balanceUpdateDao, invoiceId!=null && invoiceId.length()>0?Integer.parseInt(invoiceId):null, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
		new ArBalanceUpdateThread(balanceUpdateDao, creditNoteId, ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE).start();;
	}
	public CreditNotesVo getCreditNotesById(int id) throws ApplicationException {
		logger.info("Entry into method:getCreditNotesById");
		CreditNotesVo creditNotesVo=creditNotesDao.getCreditNotesById(id);
		if(creditNotesVo!=null && creditNotesVo.getProducts()!=null){
			creditNotesVo.setGroupedTax(arInvoiceService.calculateTotalTax(creditNotesVo.getProducts()));
		}

		if(creditNotesVo!=null  && creditNotesVo.getAttachments()!=null && creditNotesVo.getAttachments().size()>0 && creditNotesVo.getId()>0) {
			attachmentService.encodeAllFiles(creditNotesVo.getOrganizationId(), creditNotesVo.getId(),AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE, creditNotesVo.getAttachments());
		}
		return creditNotesVo;
	}

	public CreditNoteDropdownVo getCreditNoteDropdownData(int organizationId,int customerId,int creditNoteId)throws ApplicationException {
		logger.info("Entry into method:getCreditNotesById");
		return dropDownDao.getCreditNoteDropdownData(organizationId,customerId,creditNoteId);
	}

	public CustomerAccountsDropdownVo getCustomersAndAccountsDropdownData(int organizationId)throws ApplicationException {
		logger.info("Entry into method:getCreditNotesById");
		return dropDownDao.getCreditNoteCustomerAndAccountsDropdownData(organizationId);
	}

	public Map<String, Integer> getMaxAmountForOrg(Integer organizationId) throws ApplicationException {
		logger.info("Entry into getMaxAmountForOrg");
		return creditNotesDao.getMaxAmountForOrg(organizationId );
	}

	private void startWorkflow(CreditNotesVo creditNotesVo,String workflowOperation) throws ApplicationException {
		logger.info("Entry To startWorkflow");
		try {

			// Get Settings for workflow
			SettingsModuleOrganizationVo settingsModuleVo = settingsModuleOrganizationDao
					.getSettingsModuleOrganizationBySubmodule(creditNotesVo.getOrganizationId(),
							WorkflowConstants.MODULE_AR_CREDITNOTE);
			if (settingsModuleVo != null && settingsModuleVo.isRequired()) {
				if(workflowOperation!=null && workflowOperation.equalsIgnoreCase(WorkflowConstants.WORKFLOW_OPERATION_UDATE)) {
					workflowProcessService.deletePreviousRules(WorkflowConstants.MODULE_AR_CREDITNOTE,creditNotesVo.getId(), creditNotesVo.getUserId(), creditNotesVo.getRoleName());
				}

				// Get All Applicable Rules
				List<WorkflowSettingsVo> applicableRules = workflowProcessHelperService	.getApplicableRulesForModuleEntity(creditNotesVo.getOrganizationId(), creditNotesVo.getId(),WorkflowConstants.MODULE_AR_CREDITNOTE);
				if (applicableRules != null && !applicableRules.isEmpty()) {

					// Triggering Workflow
					new WorkflowThread(WorkflowConstants.MODULE_AR_CREDITNOTE, creditNotesVo.getId(),
							workflowProcessService, applicableRules,workflowOperation)
					.start();
					Thread.sleep(1000);
				}else {
					//If there are no rules applicable directly open
					if(creditNotesVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE) || (workflowOperation!=null && workflowOperation.equalsIgnoreCase(WorkflowConstants.WORKFLOW_OPERATION_CREATE))) {
						//Updating due amount in invoice
						new ArBalanceUpdateThread(balanceUpdateDao, creditNotesVo.getOriginalInvoiceId()!=null && creditNotesVo.getOriginalInvoiceId().length()>0?Integer.parseInt(creditNotesVo.getOriginalInvoiceId()):null, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
						new ArBalanceUpdateThread(balanceUpdateDao, creditNotesVo.getId(), ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE).start();;

						creditNotesDao.updateCurrentWorkflowRule(creditNotesVo.getId(), 0, CommonConstants.STATUS_AS_OPEN, null);
					}
				}

			}
	}catch(Exception e) {
		logger.error("Error in startWorkflow:",e);	
	}
}

}

