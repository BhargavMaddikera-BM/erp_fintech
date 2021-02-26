package com.blackstrawai.banking.dashboard;

import java.util.List;

public class BasicBankingVo {

	private List<BasicBankMasterWalletVo> wallets;
	
	private List<BasicBankMasterCardAccountVo> cardAccounts;
	
	private List<BasicBankMasterCashAccountVo> cashAccounts;
	
	private List<BasicBankAccountVo> bankAccounts;

	public List<BasicBankMasterWalletVo> getWallets() {
		return wallets;
	}

	public void setWallets(List<BasicBankMasterWalletVo> wallets) {
		this.wallets = wallets;
	}

	public List<BasicBankMasterCardAccountVo> getCardAccounts() {
		return cardAccounts;
	}

	public void setCardAccounts(List<BasicBankMasterCardAccountVo> cardAccounts) {
		this.cardAccounts = cardAccounts;
	}

	public List<BasicBankMasterCashAccountVo> getCashAccounts() {
		return cashAccounts;
	}

	public void setCashAccounts(List<BasicBankMasterCashAccountVo> cashAccounts) {
		this.cashAccounts = cashAccounts;
	}

	public List<BasicBankAccountVo> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BasicBankAccountVo> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	
	
}
