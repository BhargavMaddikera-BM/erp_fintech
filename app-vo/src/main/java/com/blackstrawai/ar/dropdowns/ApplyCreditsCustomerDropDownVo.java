package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ar.applycredits.CustomerBasicDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;

public class ApplyCreditsCustomerDropDownVo {

	private List<CustomerBasicDetailsVo> customerDetails;
	private List<BasicCurrencyVo> currency;
	private List<ReceiptSettingsVo> customTableList;
	private String invoiceAmountLedgerId;
	private String invoiceAmountLedgerName;
	private List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList;
	private BasicVoucherEntriesVo voucherEntries;
	private int organizationBasecurrencyId;

	public int getOrganizationBasecurrencyId() {
		return organizationBasecurrencyId;
	}

	public void setOrganizationBasecurrencyId(int organizationBasecurrencyId) {
		this.organizationBasecurrencyId = organizationBasecurrencyId;
	}

	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}

	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
	}

	public String getInvoiceAmountLedgerId() {
		return invoiceAmountLedgerId;
	}

	public void setInvoiceAmountLedgerId(String invoiceAmountLedgerId) {
		this.invoiceAmountLedgerId = invoiceAmountLedgerId;
	}

	public String getInvoiceAmountLedgerName() {
		return invoiceAmountLedgerName;
	}

	public void setInvoiceAmountLedgerName(String invoiceAmountLedgerName) {
		this.invoiceAmountLedgerName = invoiceAmountLedgerName;
	}

	public List<ArInvoiceCategoryColumnDropDownVo> getInvoiceCategoryColumnList() {
		return invoiceCategoryColumnList;
	}

	public void setInvoiceCategoryColumnList(List<ArInvoiceCategoryColumnDropDownVo> invoiceCategoryColumnList) {
		this.invoiceCategoryColumnList = invoiceCategoryColumnList;
	}

	public List<CustomerBasicDetailsVo> getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(List<CustomerBasicDetailsVo> customerDetails) {
		this.customerDetails = customerDetails;
	}

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

	public List<ReceiptSettingsVo> getCustomTableList() {
		return customTableList;
	}

	public void setCustomTableList(List<ReceiptSettingsVo> customTableList) {
		this.customTableList = customTableList;
	}

}
