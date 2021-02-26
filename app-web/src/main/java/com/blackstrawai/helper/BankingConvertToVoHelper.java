package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.banking.contra.ContraEntriesVo;
import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.request.banking.contra.ContraEntriesRequest;
import com.blackstrawai.request.banking.contra.ContraRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterAccountRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterCardRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterCashAccountRequest;
import com.blackstrawai.request.banking.dashboard.BankMasterWalletRequest;

public class BankingConvertToVoHelper {

	private static BankingConvertToVoHelper bankingConvertToVoHelper;

	public static BankingConvertToVoHelper getInstance() {
		if (bankingConvertToVoHelper == null) {
			bankingConvertToVoHelper = new BankingConvertToVoHelper();
		}
		return bankingConvertToVoHelper;
	}

	public BankMasterAccountVo convertBankMasterAccountsVoFromBankMasterAccountsRequest(
			BankMasterAccountRequest bankMasterAccountRequest) {
		BankMasterAccountVo bankMasterAccountVo = new BankMasterAccountVo();
		bankMasterAccountVo.setRoleName(bankMasterAccountRequest.getRoleName());
		bankMasterAccountVo.setAccountCode(bankMasterAccountRequest.getAccountCode());
		bankMasterAccountVo.setAccountCurrencyId(bankMasterAccountRequest.getAccountCurrencyId());
		bankMasterAccountVo.setAccountName(bankMasterAccountRequest.getAccountName());
		bankMasterAccountVo.setAccountNumber(bankMasterAccountRequest.getAccountNumber());
		bankMasterAccountVo.setAccountType(bankMasterAccountRequest.getAccountType());
		bankMasterAccountVo.setAccountVariant(bankMasterAccountRequest.getAccountVariant());
		bankMasterAccountVo.setBankName(bankMasterAccountRequest.getBankName());
		bankMasterAccountVo.setBranchName(bankMasterAccountRequest.getBranchName());
		bankMasterAccountVo.setCurrentBalance(bankMasterAccountRequest.getCurrentBalance());
		bankMasterAccountVo.setId(bankMasterAccountRequest.getId());
		bankMasterAccountVo.setIfscCode(bankMasterAccountRequest.getIfscCode());
		bankMasterAccountVo.setInterestRate(bankMasterAccountRequest.getInterestRate());
		bankMasterAccountVo.setIsSuperAdmin(bankMasterAccountRequest.getIsSuperAdmin());
		bankMasterAccountVo.setLimit(bankMasterAccountRequest.getLimit());
		bankMasterAccountVo.setMaturityDate(bankMasterAccountRequest.getMaturityDate());
		bankMasterAccountVo.setOpeningDate(bankMasterAccountRequest.getOpeningDate());
		bankMasterAccountVo.setOrganizationId(bankMasterAccountRequest.getOrganizationId());
		bankMasterAccountVo.setTermMonth(bankMasterAccountRequest.getTermMonth());
		bankMasterAccountVo.setTermYear(bankMasterAccountRequest.getTermYear());
		bankMasterAccountVo.setUserId(bankMasterAccountRequest.getUserId());
		return bankMasterAccountVo;
	}

	public BankMasterCardVo convertBankMasterCardsVoFromBankMasterCardsRequest(
			BankMasterCardRequest bankMasterCardRequest) {
		BankMasterCardVo bankMasterCardVo = new BankMasterCardVo();
		bankMasterCardVo.setRoleName(bankMasterCardRequest.getRoleName());
		bankMasterCardVo.setAccountCode(bankMasterCardRequest.getAccountCode());
		bankMasterCardVo.setAccountName(bankMasterCardRequest.getAccountName());
		bankMasterCardVo.setAuthorizedPerson(bankMasterCardRequest.getAuthorizedPerson());
		bankMasterCardVo.setBillingDate(bankMasterCardRequest.getBillingDate());
		bankMasterCardVo.setCardNumber(bankMasterCardRequest.getCardNumber());
		bankMasterCardVo.setCurrentBalance(bankMasterCardRequest.getCurrentBalance());
		bankMasterCardVo.setId(bankMasterCardRequest.getId());
		bankMasterCardVo.setIssuingBankName(bankMasterCardRequest.getIssuingBankName());
		bankMasterCardVo.setIsSuperAdmin(bankMasterCardRequest.getIsSuperAdmin());
		bankMasterCardVo.setLimit(bankMasterCardRequest.getLimit());
		bankMasterCardVo.setOpeningDate(bankMasterCardRequest.getOpeningDate());
		bankMasterCardVo.setOrganizationId(bankMasterCardRequest.getOrganizationId());
		bankMasterCardVo.setUserId(bankMasterCardRequest.getUserId());
		return bankMasterCardVo;
	}

	public BankMasterWalletVo convertBankMasterWalletsVoFromBankMasterWalletsRequest(
			BankMasterWalletRequest bankMasterWalletRequest) {
		BankMasterWalletVo bankMasterWalletVo = new BankMasterWalletVo();
		bankMasterWalletVo.setRoleName(bankMasterWalletRequest.getRoleName());
		bankMasterWalletVo.setAccountCode(bankMasterWalletRequest.getAccountCode());
		bankMasterWalletVo.setAuthorizedPerson(bankMasterWalletRequest.getAuthorizedPerson());
		bankMasterWalletVo.setCurrentBalance(bankMasterWalletRequest.getCurrentBalance());
		bankMasterWalletVo.setId(bankMasterWalletRequest.getId());
		bankMasterWalletVo.setIsSuperAdmin(bankMasterWalletRequest.getIsSuperAdmin());
		bankMasterWalletVo.setOpeningDate(bankMasterWalletRequest.getOpeningDate());
		bankMasterWalletVo.setOrganizationId(bankMasterWalletRequest.getOrganizationId());
		bankMasterWalletVo.setTransactionLimit(bankMasterWalletRequest.getTransactionLimit());
		bankMasterWalletVo.setWalletAccountName(bankMasterWalletRequest.getWalletAccountName());
		bankMasterWalletVo.setWalletNumber(bankMasterWalletRequest.getWalletNumber());
		bankMasterWalletVo.setWalletProvider(bankMasterWalletRequest.getWalletProvider());
		bankMasterWalletVo.setUserId(bankMasterWalletRequest.getUserId());
		return bankMasterWalletVo;

	}

	public BankMasterCashAccountVo convertbankMasterCashAccountVoFromBankMasterCashAccountRequest(
			BankMasterCashAccountRequest bankMasterCashAccountRequest) {
		BankMasterCashAccountVo bankMasterCashAccountVo = new BankMasterCashAccountVo();
		bankMasterCashAccountVo.setRoleName(bankMasterCashAccountRequest.getRoleName());
		bankMasterCashAccountVo.setAccountCode(bankMasterCashAccountRequest.getAccountCode());
		bankMasterCashAccountVo.setCashAccountName(bankMasterCashAccountRequest.getCashAccountName());
		bankMasterCashAccountVo.setCurrencyId(bankMasterCashAccountRequest.getCurrencyId());
		bankMasterCashAccountVo.setCurrentBalance(bankMasterCashAccountRequest.getCurrentBalance());
		bankMasterCashAccountVo.setId(bankMasterCashAccountRequest.getId());
		bankMasterCashAccountVo.setIsSuperAdmin(bankMasterCashAccountRequest.getIsSuperAdmin());
		bankMasterCashAccountVo.setLocationId(bankMasterCashAccountRequest.getLocationId());
		bankMasterCashAccountVo.setOrganizationId(bankMasterCashAccountRequest.getOrganizationId());
		bankMasterCashAccountVo.setStatus(bankMasterCashAccountRequest.getStatus());
		bankMasterCashAccountVo.setUserId(bankMasterCashAccountRequest.getUserId());
		return bankMasterCashAccountVo;
	}

	public ContraVo convertContraAccountsVoFromContraAccountsRequest(ContraRequest contraRequest) {
		ContraVo contraVo = new ContraVo();
		List<ContraEntriesVo> listContraGeneralInfo = new ArrayList<ContraEntriesVo>();
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		contraVo.setReferenceNo(contraRequest.getReferenceNo());
		contraVo.setRoleName(contraRequest.getRoleName());
		contraVo.setDate(contraRequest.getDate());
		contraVo.setBaseCurrency(contraRequest.getBaseCurrency());
		contraVo.setRemark(contraRequest.getRemark());
		contraVo.setTotalCredit(contraRequest.getTotalCredit());
		contraVo.setTotalDebit(contraRequest.getTotalDebit());
		contraVo.setIsSuperAdmin(contraRequest.getIsSuperAdmin());
		contraVo.setId(contraRequest.getId());
		contraVo.setOrgId(contraRequest.getOrgId());
		contraVo.setStatus(contraRequest.getStatus());
		contraVo.setUserId(contraRequest.getUserId());
		contraVo.setDifference(contraRequest.getDifference());
		for (ContraEntriesRequest contraGeneralInforequest : contraRequest.getContraEntries()) {
			ContraEntriesVo contraGeneralInfoVo = new ContraEntriesVo();
			contraGeneralInfoVo.setAccountId(contraGeneralInforequest.getAccountId());
			contraGeneralInfoVo.setCredit(contraGeneralInforequest.getCredit());
			contraGeneralInfoVo.setCurrencyId(contraGeneralInforequest.getCurrencyId());
			contraGeneralInfoVo.setDebit(contraGeneralInforequest.getDebit());
			contraGeneralInfoVo.setExchangeRate(contraGeneralInforequest.getExchangeRate());
			contraGeneralInfoVo.setId(contraGeneralInforequest.getId());
			contraGeneralInfoVo.setStatus(contraGeneralInforequest.getStatus());
			contraGeneralInfoVo.setTotalCreditsEx(contraGeneralInforequest.getTotalCreditsEx());
			contraGeneralInfoVo.setTotalDebitsEx(contraGeneralInforequest.getTotalDebitsEx());
			contraGeneralInfoVo.setAccountType(contraGeneralInforequest.getAccountType());
			contraGeneralInfoVo.setTransRefNo(contraGeneralInforequest.getTransRefNo());
			listContraGeneralInfo.add(contraGeneralInfoVo);
		}
		if (contraRequest.getAttachments() != null && contraRequest.getAttachments().size() > 0) {
			contraRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		contraVo.setAttachments(uploadList);
		contraVo.setAttachmentsToRemove(contraRequest.getAttachmentsToRemove());
		contraVo.setContraEntries(listContraGeneralInfo);
		contraVo.setItemsToRemove(contraRequest.getItemsToRemove());
		return contraVo;
	}


}
