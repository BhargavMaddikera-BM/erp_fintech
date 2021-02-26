package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.accounting.AccountingAspectsApproversVo;
import com.blackstrawai.accounting.AccountingAspectsFilterVo;
import com.blackstrawai.accounting.AccountingAspectsGeneralVo;
import com.blackstrawai.accounting.AccountingAspectsItemsVo;
import com.blackstrawai.accounting.AccountingAspectsVo;
import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.request.accounting.AccountingAspectsFilterRequest;
import com.blackstrawai.request.accounting.AccountingAspectsRequest;
import com.blackstrawai.request.accounting.ledger.LedgerRequest;

public class AccountingConvertToVoHelper {


	private static AccountingConvertToVoHelper accountingConvertToVoHelper;

	public static AccountingConvertToVoHelper getInstance() {
		if (accountingConvertToVoHelper == null) {
			accountingConvertToVoHelper = new AccountingConvertToVoHelper();
		}
		return accountingConvertToVoHelper;
	}


	public AccountingAspectsVo convertAccountingAspectsVoFromAccountingAspectsRequest(
			AccountingAspectsRequest accountingAspectsRequest) {
		AccountingAspectsVo accountingAspectsVo = new AccountingAspectsVo();
		accountingAspectsVo.setRoleName(accountingAspectsRequest.getRoleName());
		accountingAspectsVo.setItemsToRemove(accountingAspectsRequest.getItemsToRemove());
		accountingAspectsVo.setAttachmentsToRemove(accountingAspectsRequest.getAttachmentsToRemove());
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		accountingAspectsVo.setIsSuperAdmin(accountingAspectsRequest.getIsSuperAdmin());
		accountingAspectsVo.setOrganizationId(accountingAspectsRequest.getOrganizationId());
		accountingAspectsVo.setUserId(accountingAspectsRequest.getUserId());
		accountingAspectsVo.setId(accountingAspectsRequest.getId());
		accountingAspectsVo.setStatus(accountingAspectsRequest.getStatus());
		AccountingAspectsGeneralVo accountingAspectsGeneralVo = new AccountingAspectsGeneralVo();
		AccountingAspectsApproversVo approverslist = new AccountingAspectsApproversVo();
		if (accountingAspectsRequest.getApproversList() != null) {
			approverslist.setApprover1(accountingAspectsRequest.getApproversList().getApprover1());
			approverslist.setApprover2(accountingAspectsRequest.getApproversList().getApprover2());
			approverslist.setApprover3(accountingAspectsRequest.getApproversList().getApprover3());
		}
		accountingAspectsVo.setApproversList(approverslist);
		accountingAspectsGeneralVo
		.setCurrencyId(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getCurrencyId());
		accountingAspectsGeneralVo
		.setDifference(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getDifference());
		accountingAspectsGeneralVo
		.setDateOfCreation(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getDateOfCreation());
		accountingAspectsGeneralVo
		.setJournalNo(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getJournalNo());
		accountingAspectsGeneralVo
		.setLocationId(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getLocationId());
		accountingAspectsGeneralVo.setNotes(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getNotes());
		accountingAspectsGeneralVo
		.setTotalCredits(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getTotalCredits());
		accountingAspectsGeneralVo
		.setTotalDebits(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getTotalDebits());
		accountingAspectsGeneralVo.setTypeId(accountingAspectsRequest.getAccountingAspectsGeneralInfo().getTypeId());
		accountingAspectsGeneralVo.setIsRegisteredLocation(
				accountingAspectsRequest.getAccountingAspectsGeneralInfo().getIsRegisteredLocation());
		accountingAspectsVo.setAccountingAspectsGeneralInfo(accountingAspectsGeneralVo);
		List<AccountingAspectsItemsVo> itemsList = new ArrayList<AccountingAspectsItemsVo>();
		for (int i = 0; i < accountingAspectsRequest.getItemDetails().size(); i++) {
			AccountingAspectsItemsVo singleItem = new AccountingAspectsItemsVo();
			singleItem.setId(accountingAspectsRequest.getItemDetails().get(i).getId());
			singleItem.setAccountsId(accountingAspectsRequest.getItemDetails().get(i).getAccountsId());
			singleItem.setCurrencyId(accountingAspectsRequest.getItemDetails().get(i).getCurrencyId());
			singleItem.setExchangeRate(accountingAspectsRequest.getItemDetails().get(i).getExchangeRate());
			singleItem.setCredits(accountingAspectsRequest.getItemDetails().get(i).getCredits());
			singleItem.setDebits(accountingAspectsRequest.getItemDetails().get(i).getDebits());
			singleItem.setDescription(accountingAspectsRequest.getItemDetails().get(i).getDescription());
			singleItem.setSubLedgerId(accountingAspectsRequest.getItemDetails().get(i).getSubLedgerId());
			singleItem.setAccountsName(accountingAspectsRequest.getItemDetails().get(i).getAccountsName());
			singleItem.setAccountsLevel(accountingAspectsRequest.getItemDetails().get(i).getAccountsLevel());
			singleItem.setSubLedgerName(accountingAspectsRequest.getItemDetails().get(i).getSubLedgerName());
			singleItem.setStatus(accountingAspectsRequest.getItemDetails().get(i).getStatus());
			singleItem.setTotalCreditsEx(accountingAspectsRequest.getItemDetails().get(i).getTotalCreditsEx());
			singleItem.setTotalDebitsEx(accountingAspectsRequest.getItemDetails().get(i).getTotalDebitsEx());
			itemsList.add(singleItem);
		}
		if (accountingAspectsRequest.getAttachments() != null && accountingAspectsRequest.getAttachments().size() > 0) {
			accountingAspectsRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		accountingAspectsVo.setAttachments(uploadList);
		accountingAspectsVo.setItemDetails(itemsList);
		return accountingAspectsVo;
	}		


	public AccountingAspectsFilterVo convertAccountingAspectsFilterVoFromAccountingAspectsFilterRequest(
			AccountingAspectsFilterRequest accountingAspectsFilterRequest) {
		AccountingAspectsFilterVo accountingAspectsFilterVo = new AccountingAspectsFilterVo();
		accountingAspectsFilterVo.setUserId(accountingAspectsFilterRequest.getUserId());
		accountingAspectsFilterVo.setOrgId(accountingAspectsFilterRequest.getOrgId());
		accountingAspectsFilterVo.setStartDate(accountingAspectsFilterRequest.getStartDate());
		accountingAspectsFilterVo.setStatus(accountingAspectsFilterRequest.getStatus());
		accountingAspectsFilterVo.setEndDate(accountingAspectsFilterRequest.getEndDate());
		accountingAspectsFilterVo.setRoleName(accountingAspectsFilterRequest.getRoleName());
		return accountingAspectsFilterVo;
	}


	public LedgerVo convertLedgerVoFromRequest(LedgerRequest ledgerRequest) {
		LedgerVo ledgerVo = new LedgerVo();
		ledgerVo.setRoleName(ledgerRequest.getRoleName());
		ledgerVo.setId(ledgerRequest.getId());
		ledgerVo.setUserId(ledgerRequest.getUserId());
		ledgerVo.setOrganizationId(ledgerRequest.getOrganizationId());
		ledgerVo.setAccountGroupId(ledgerRequest.getAccountGroupId());
		ledgerVo.setAccountNameId(ledgerRequest.getAccountNameId());
		ledgerVo.setLedgerName(ledgerRequest.getLedgerName());
		ledgerVo.setAccountCode(ledgerRequest.getAccountCode());
		ledgerVo.setDateOfCreation(ledgerRequest.getDateOfCreation());
		ledgerVo.setLedgerBalance(ledgerRequest.getLedgerBalance());
		ledgerVo.setLedgerStatus(ledgerRequest.getLedgerStatus());
		ledgerVo.setModuleId(ledgerRequest.getModuleId());
		ledgerVo.setIsSubledgerMandatory(ledgerRequest.getIsSubledgerMandatory());
		ledgerVo.setIsBase(ledgerRequest.getIsBase());
		ledgerVo.setEntityId(ledgerRequest.getEntityId() != null ? ledgerRequest.getEntityId() : 0);
		ledgerVo.setIsSuperAdmin(ledgerRequest.getIsSuperAdmin());
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (ledgerRequest.getAttachments() != null && ledgerRequest.getAttachments().size() > 0) {
			ledgerRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		ledgerVo.setAttachments(uploadList);
		ledgerVo.setAttachmentsToRemove(ledgerRequest.getAttachmentsToRemove());
		return ledgerVo;

	}


}
