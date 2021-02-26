package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicVendorDetailsVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ar.receipt.ReceiptCommonVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ReceiptDropdownVo {
	private List<ReceiptCommonVo> customersList;
	private List<BankMasterAccountBaseVo> bankList;
	private List<MinimalChartOfAccountsVo> customerLedgersList;
	private BasicVoucherEntriesVo receiptNo;
	private List<ReceiptTypeVo> receiptTypesList;
	private List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList;
	private List<ReceiptSettingsVo> settingsData;
	private List<BasicCurrencyVo> currency;
	private int currencyId;
	private List<BasicVendorDetailsVo> vendorList;
	private List<ReceiptCommonVo> contactList;


	public List<ReceiptCommonVo> getContactList() {
		return contactList;
	}

	public void setContactList(List<ReceiptCommonVo> contactList) {
		this.contactList = contactList;
	}

	public List<BasicVendorDetailsVo> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<BasicVendorDetailsVo> vendorList) {
		this.vendorList = vendorList;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}


	
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}


	public List<ArInvoiceCategoryColumnDropDownVo> getInvoiceCategoryColumnList() {
		return invoiceCategoryColumnList;
	}
	public void setInvoiceCategoryColumnList(List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList) {
		this.invoiceCategoryColumnList = invoiceCategoryColumnList;
	}
	public List<ReceiptCommonVo> getCustomersList() {
		return customersList;
	}
	public void setCustomersList(List<ReceiptCommonVo> customersList) {
		this.customersList = customersList;
	}

	
	public List<ReceiptSettingsVo> getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(List<ReceiptSettingsVo> settingsData) {
		this.settingsData = settingsData;
	}

	public List<BankMasterAccountBaseVo> getBankList() {
		return bankList;
	}
	public void setBankList(List<BankMasterAccountBaseVo> bankList) {
		this.bankList = bankList;
	}
	
	public List<MinimalChartOfAccountsVo> getCustomerLedgersList() {
		return customerLedgersList;
	}
	public void setCustomerLedgersList(List<MinimalChartOfAccountsVo> customerLedgersList) {
		this.customerLedgersList = customerLedgersList;
	}
	public BasicVoucherEntriesVo getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(BasicVoucherEntriesVo receiptNo) {
		this.receiptNo = receiptNo;
	}

	public List<ReceiptTypeVo> getReceiptTypesList() {
		return receiptTypesList;
	}

	public void setReceiptTypesList(List<ReceiptTypeVo> receiptTypesList) {
		this.receiptTypesList = receiptTypesList;
	}
	
	
	

}
