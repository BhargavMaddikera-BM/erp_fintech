package com.blackstrawai.banking.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;

public class ContraDropDownVo {

	private List<BankMasterAccountBaseVo> account;
	private List<BasicCurrencyVo> currency;
	private BasicCurrencyVo baseCurrency;
	private BasicVoucherEntriesVo voucherEntries;


	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}

	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
	}

	public List<BankMasterAccountBaseVo> getAccount() {
		return account;
	}

	public void setAccount(List<BankMasterAccountBaseVo> account) {
		this.account = account;
	}

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

	public BasicCurrencyVo getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(BasicCurrencyVo baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

}
