package com.blackstrawai.accounting.dropdowns;

import java.util.List;

import com.blackstrawai.accounting.AccountingAspectsTypeVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel1Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel6Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class AccountingAspectsDropDownVo {

	private List<AccountingAspectsTypeVo> accountingtypes;
	private List<BasicCurrencyVo> currency;
	private List<UserVo>level1User;
	private List<UserVo>level2User;
	private List<UserVo>level3User;
	private BasicVoucherEntriesVo voucherEntries;
	private BasicCurrencyVo defaultCurrency;
	private String dateFormat;
	private BasicGSTLocationDetailsVo location;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	private List<MinimalChartOfAccountsLevel6Vo>subLedger;
	//private Map<String,List<Integer>> subLedger;
	private List<ChartOfAccountsLevel1Vo> multiLevelAccounts;
	private BasicVoucherEntriesVo payrollVoucherEntries;
	
	public BasicVoucherEntriesVo getPayrollVoucherEntries() {
		return payrollVoucherEntries;
	}

	public void setPayrollVoucherEntries(BasicVoucherEntriesVo payrollVoucherEntries) {
		this.payrollVoucherEntries = payrollVoucherEntries;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public BasicCurrencyVo getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(BasicCurrencyVo defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}

	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
	}

	public List<UserVo> getLevel1User() {
		return level1User;
	}

	public void setLevel1User(List<UserVo> level1User) {
		this.level1User = level1User;
	}

	public List<UserVo> getLevel2User() {
		return level2User;
	}

	public void setLevel2User(List<UserVo> level2User) {
		this.level2User = level2User;
	}

	public List<UserVo> getLevel3User() {
		return level3User;
	}

	public void setLevel3User(List<UserVo> level3User) {
		this.level3User = level3User;
	}

	
	public List<MinimalChartOfAccountsLevel6Vo> getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(List<MinimalChartOfAccountsLevel6Vo> subLedger) {
		this.subLedger = subLedger;
	}

	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	

	public BasicGSTLocationDetailsVo getLocation() {
		return location;
	}

	public void setLocation(BasicGSTLocationDetailsVo location) {
		this.location = location;
	}

	

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

	public List<AccountingAspectsTypeVo> getAccountingtypes() {
		return accountingtypes;
	}

	public void setAccountingtypes(List<AccountingAspectsTypeVo> accountingtypes) {
		this.accountingtypes = accountingtypes;
	}

	public List<ChartOfAccountsLevel1Vo> getMultiLevelAccounts() {
		return multiLevelAccounts;
	}

	public void setMultiLevelAccounts(List<ChartOfAccountsLevel1Vo> multiLevelAccounts) {
		this.multiLevelAccounts = multiLevelAccounts;
	}

	
	
}
