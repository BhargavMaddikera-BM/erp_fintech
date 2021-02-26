package com.blackstrawai.ar;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.dropdowns.BasicInvoicesDropdownVo;
import com.blackstrawai.ar.dropdowns.RefundDropdownVo;
import com.blackstrawai.ar.refund.RefundVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.banking.BankMasterService;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;

@Service
public class RefundService extends BaseService {

	@Autowired
	RefundDao refundDao;

	@Autowired
	FinanceCommonDao financeCommonDao;

	@Autowired
	DropDownDao dropDownDao;

	@Autowired
	BankMasterService bankMasterService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	ArBalanceUpdateDao balanceUpdateDao;
	
	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	private Logger logger = Logger.getLogger(RefundService.class);

	public RefundVo createRefund(RefundVo refundVo) throws ApplicationException {
		logger.info("Entry into method:createRefund");
		refundVo = refundDao.createRefund(refundVo);
		if (refundVo != null) {
			if (refundVo.getId() > 0 && refundVo.getAttachments() != null && !refundVo.getAttachments().isEmpty()) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND, refundVo.getId(),
						refundVo.getOrganizationId(), refundVo.getAttachments());
				logger.info("Upload Successfull");
			}
			new ArBalanceUpdateThread(balanceUpdateDao, refundVo.getInvoiceReferenceId(),
					ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
			

		}

		logger.info("Before thread");
		if(refundVo.getId()!=0  && (CommonConstants.STATUS_AS_ACTIVE.equals(refundVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(refundVo.getStatus()) )) {
			logger.info("To thread");
			JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, refundVo.getId(), refundVo.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_REFUNDS);
			journalThread.start();
		}

		return refundVo;
	}

	public List<RefundVo> getAllRefundsOfAnOrganization(int OrganizationId, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method:getAllRefundsOfAnOrganization");
		List<RefundVo> refundList = refundDao.getAllRefundsOfAnOrganization(OrganizationId, userId, roleName);

		return refundList;
	}

	public RefundVo deleteRefund(int id, String status, String userId, String roleName) throws ApplicationException {
		logger.info("Entry into method:deleteRefund");
		RefundVo refundVo=refundDao.deleteRefund(id, status, userId, roleName);
		if(refundVo!=null && refundVo.getId()>0) {
			int invoiceId=refundDao.getInvoiceForRefund(refundVo.getId());
			new ArBalanceUpdateThread(balanceUpdateDao, invoiceId, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();

		}
		return refundVo;
	}

	public RefundVo updateRefund(RefundVo refundVo) throws ApplicationException {
		logger.info("Entry into method:updateRefund");
		refundVo = refundDao.updateRefund(refundVo);
		if (refundVo != null) {
			if (refundVo.getAttachments() != null && !refundVo.getAttachments().isEmpty() && refundVo.getId() > 0) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND, refundVo.getId(),
						refundVo.getOrganizationId(), refundVo.getAttachments());
				logger.info("Upload Successfull");
			}
			new ArBalanceUpdateThread(balanceUpdateDao, refundVo.getInvoiceReferenceId(),
					ArInvoiceConstants.MODULE_TYPE_AR_INVOICES).start();
	

			logger.info("Before thread");
			if(refundVo.getId()!=0  && (CommonConstants.STATUS_AS_ACTIVE.equals(refundVo.getStatus()) || CommonConstants.STATUS_AS_OPEN.equals(refundVo.getStatus()) )) {
				logger.info("To thread");
				JournalEntriesThread journalThread =new JournalEntriesThread(journalEntriesTransactionDao, refundVo.getId(), refundVo.getOrganizationId(),JournalEntriesConstants.SUB_MODULE_REFUNDS);
				journalThread.start();
			}

			
		}
		return refundVo;
	}

	public RefundVo getRefundById(int id) throws ApplicationException {
		logger.info("Entry into method: getRefundById");
		RefundVo refundVo = refundDao.getRefundById(id);
		String paymentMode = refundVo.getPaymentMode();
		if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
			BankMasterCashAccountVo vo = bankMasterService.getBankMasterCashAccountById(refundVo.getPaymentAccountId());
			refundVo.setPaymentAccountName(vo != null ? vo.getCashAccountName() : null);
		} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
				|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
			BankMasterAccountVo vo = bankMasterService.getBankMasterAccountsById(refundVo.getPaymentAccountId());
			refundVo.setPaymentAccountName(vo != null ? vo.getAccountName() : null);
		}
		if (refundVo != null && refundVo.getAttachments() != null && refundVo.getAttachments().size() > 0
				&& refundVo.getId() > 0) {
			attachmentService.encodeAllFiles(refundVo.getOrganizationId(), refundVo.getId(),
					AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND, refundVo.getAttachments());
		}
		return refundVo;
	}

	public RefundDropdownVo getRefundDropdownData(int organizationId) throws ApplicationException {
		logger.info("Entry into method:getRefundDropdownData");

		return dropDownDao.getRefundDropDownData(organizationId);
	}

	public BasicInvoicesDropdownVo getRefundInvoicesForCustomer(int organizationId, int customerId, int refundId)
			throws ApplicationException {
		logger.info("Entry into method:getRefundDropdownData");
		return dropDownDao.getRefundInvoicesForCustomer(organizationId, customerId,refundId);
	}

}
