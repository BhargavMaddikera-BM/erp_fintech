package com.blackstrawai.accounting;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.accounting.dropdowns.AccountingAspectsDropDownVo;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.chartofaccounts.ChartOfAccountsDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.export.AccountingEntryExportVo;
import com.blackstrawai.export.ExportVo;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.journals.JournalEntriesThread;
import com.blackstrawai.journals.JournalEntriesTransactionDao;
import com.blackstrawai.onboarding.OrganizationService;
import com.blackstrawai.settings.CurrencyService;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLedgerDetailsVo;
import com.blackstrawai.upload.AccountingEntriesUploadVo;

@Service
public class AccountingAspectsService extends BaseService {

	@Autowired
	AccountingAspectsDao accountingAspectsDao;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	CurrencyService currencyService;

	@Autowired
	OrganizationService organizationService;

	@Autowired
	DropDownDao dropDownDao;
	
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;

	@Autowired
	private JournalEntriesTransactionDao journalEntriesTransactionDao;

	private Logger logger = Logger.getLogger(AccountingAspectsService.class);

	public void createAccountingAspects(AccountingAspectsVo accountingAspectsVo) throws ApplicationException {
		logger.info("Entry into createAccountingAspects");
		try {
			accountingAspectsVo = accountingAspectsDao.createAccountingAspects(accountingAspectsVo);
			if (accountingAspectsVo.getId() != null && !accountingAspectsVo.getAttachments().isEmpty()
					&& accountingAspectsVo.getId() != null) {
				logger.info("Entry into upload");
				attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS,
						accountingAspectsVo.getId(), accountingAspectsVo.getOrganizationId(),
						accountingAspectsVo.getAttachments());
				logger.info("Upload Successfull");
			}
			if ((accountingAspectsVo.getId() != null) && (accountingAspectsVo.getStatus().equals("ACT")|| accountingAspectsVo.getStatus().equals("ACTIVE"))) {
				logger.info("To thread");
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						accountingAspectsVo.getId(), accountingAspectsVo.getOrganizationId(),
						JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS);
				journalThread.start();
			}
			logger.info("AccountingAspects created Successfully in service layer ");
		} catch (Exception e) {
			logger.info("Error in AccountingAspects create in service layer ");
			try {
				accountingAspectsDao.deleteAccountingEntries(accountingAspectsVo.getId(),accountingAspectsVo.getUserId(),accountingAspectsVo.getRoleName());
			} catch (Exception e1) {
				logger.info("Error in Accounting Aspects delete in service layer ");
				throw new ApplicationException(e);
			}
			throw new ApplicationException(e);
		}
	}

	public void deleteAccountingAspects(Integer id, String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteAccountingAspects");
		accountingAspectsDao.deleteAccountingAspects(id, status,userId,roleName);
	}

/*	public List<AccountingAspectsBasicVo> getAllAccountingAspectsOfAnOrganization(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getAllAccountingAspectsOfAnOrganization");
		List<AccountingAspectsBasicVo> listofacountingAspects = accountingAspectsDao
				.getAllAccountingAspectsOfAnOrganization(organizationId);
		Collections.reverse(listofacountingAspects);
		return listofacountingAspects;
	}*/
	
	public List<AccountingAspectsBasicVo> getAllAccountingAspectsOfAnOrganizationForUserAndRole(int organizationId,String userId,String roleName)
			throws ApplicationException {
		logger.info("Entry into method: getAllAccountingAspectsOfAnOrganizationForUserAndRole");
		List<AccountingAspectsBasicVo> listofacountingAspects = accountingAspectsDao
				.getAllAccountingAspectsOfAnOrganizationForUserAndRole(organizationId,userId,roleName);
		Collections.reverse(listofacountingAspects);
		return listofacountingAspects;
	}

	public AccountingAspectsVo getAccountingAspectsById(Integer id) throws ApplicationException {
		logger.info("Entry into method: getAccountingAspectsById");
		AccountingAspectsVo accountingAspectsVo;
		try {
			accountingAspectsVo = accountingAspectsDao.getAccountingAspectsById(id);
			if (accountingAspectsVo != null && accountingAspectsVo.getAttachments().size() > 0
					&& accountingAspectsVo.getId() != null) {
				attachmentService.encodeAllFiles(accountingAspectsVo.getOrganizationId(), accountingAspectsVo.getId(),
						AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS, accountingAspectsVo.getAttachments());
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return accountingAspectsVo;
	}
	
	public ChartOfAccountsLedgerDetailsVo getLedgerDetailsByLedgerId(Integer ledgerId,Integer orgId) throws ApplicationException {
		logger.info("Entry into method: getLedgerDetailsByLedgerId");
		return chartOfAccountsDao.getChartOfAccountsByLedgerId(ledgerId,orgId);
		
	}

	public void updateAccountingAspects(AccountingAspectsVo accountingAspectsVo) throws ApplicationException {
		logger.info("Entry into updateAccountingAspects");
		if (accountingAspectsVo.getId() != null) {
			try {
				accountingAspectsVo = accountingAspectsDao.updateAccountingAspects(accountingAspectsVo);
				if (accountingAspectsVo.getId() != null && !accountingAspectsVo.getAttachments().isEmpty()
						&& accountingAspectsVo.getId() != null) {
					logger.info("Entry into upload");
					attachmentService.upload(AttachmentsConstants.MODULE_TYPE_ACCOUNTING_ASPECTS,
							accountingAspectsVo.getId(), accountingAspectsVo.getOrganizationId(),
							accountingAspectsVo.getAttachments());
					logger.info("Upload Successfull");
				}
				if ((accountingAspectsVo.getId() != null) && (accountingAspectsVo.getStatus().equals("ACT")|| accountingAspectsVo.getStatus().equals("ACTIVE"))) {
					logger.info("To thread");
					JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
							accountingAspectsVo.getId(), accountingAspectsVo.getOrganizationId(),
							JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS);
					journalThread.start();
				}

			} catch (Exception e) {
				throw new ApplicationException(e);
			}
		}
	}

	public AccountingAspectsDropDownVo getAccountingAspectsDropDownData(int organizationId)
			throws ApplicationException {
		logger.info("Entry into method: getAccountingAspectsDropDownData");
		AccountingAspectsDropDownVo accountingAspectsDropDownVo = dropDownDao
				.getAccountingAspectsDropDownData(organizationId);
		return accountingAspectsDropDownVo;
	}

	public List<AccountingAspectsBasicVo> getAccountingAspectsFilterData(
			AccountingAspectsFilterVo accountingAspectsFilterVo) throws ApplicationException {
		logger.info("Entry into getAccountingAspectsFilterData");
		return accountingAspectsDao.getAccountingAspectsFilterData(accountingAspectsFilterVo);
	}

	public Map<String, String> processUpload(List<AccountingEntriesUploadVo> accountingEntryList, Integer orgId,
			String userId, boolean isSuperAdmin,boolean duplicacy,String roleName) throws ApplicationException {
		logger.info("Entry into processUpload");
		Map<String, String> map = new HashMap<String, String>();
		try {
			AccountingEntriesUploadVo accountingEntriesUploadVo=accountingAspectsDao.processUpload(accountingEntryList, orgId, userId, isSuperAdmin,duplicacy,roleName);
			if(accountingEntriesUploadVo!=null && accountingEntriesUploadVo.getStatus().equals("ACT")){
				JournalEntriesThread journalThread = new JournalEntriesThread(journalEntriesTransactionDao,
						Integer.parseInt(accountingEntriesUploadVo.getJournalNo()), orgId,
						JournalEntriesConstants.VOUCHER_TYPE_ACCOUNTING_ASPECTS);
				journalThread.start();
			}
			map.put("Success", "Success");
			return map;
		} catch (Exception e) {
			map.put("Failure", e.getMessage());
			logger.info("exception inside Accouning Entry service: " + map.toString());
			return map;
		}
	}

	public List<AccountingEntryExportVo> getListAccountingEntriesById(ExportVo exportVo) throws ApplicationException {
		logger.info("Entry into getListAccountingEntriesById");
		List<AccountingEntryExportVo> accountingEntries = accountingAspectsDao.getListAccountingEntriesById(exportVo);
		return accountingEntries;
	}

}
