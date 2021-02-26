package com.blackstrawai.banking.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountTypeVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountVariantVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;

public class BankMasterDropDownVo extends BaseVo {

	private List<BankMasterAccountTypeVo> accountType;
	private List<BankMasterAccountVariantVo> accountVariant;
	private List<BasicCurrencyVo> currency;
	private BasicGSTLocationDetailsVo location;
	private String baseCurrencyName;

	public List<BankMasterAccountTypeVo> getAccountType() {
		return accountType;
	}

	public BasicGSTLocationDetailsVo getLocation() {
		return location;
	}

	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}

	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}

	public void setLocation(BasicGSTLocationDetailsVo location) {
		this.location = location;
	}

	public void setAccountType(List<BankMasterAccountTypeVo> accountType) {
		this.accountType = accountType;
	}

	public List<BankMasterAccountVariantVo> getAccountVariant() {
		return accountVariant;
	}

	public void setAccountVariant(List<BankMasterAccountVariantVo> accountVariant) {
		this.accountVariant = accountVariant;
	}

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

}
