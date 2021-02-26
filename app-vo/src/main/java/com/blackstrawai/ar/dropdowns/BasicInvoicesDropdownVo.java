package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class BasicInvoicesDropdownVo {
	
	private List<BasicInvoiceDetailsVo> invoiceList;
	private List<MinimalChartOfAccountsVo> invoiceLedgers;
	private String advanceBalance;
	private String creditNotesBalance;

	
	
	public String getAdvanceBalance() {
		return advanceBalance;
	}

	public void setAdvanceBalance(String advanceBalance) {
		this.advanceBalance = advanceBalance;
	}

	public String getCreditNotesBalance() {
		return creditNotesBalance;
	}

	public void setCreditNotesBalance(String creditNotesBalance) {
		this.creditNotesBalance = creditNotesBalance;
	}

	public List<MinimalChartOfAccountsVo> getInvoiceLedgers() {
		return invoiceLedgers;
	}

	public void setInvoiceLedgers(List<MinimalChartOfAccountsVo> invoiceLedgers) {
		this.invoiceLedgers = invoiceLedgers;
	}

	public List<BasicInvoiceDetailsVo> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<BasicInvoiceDetailsVo> invoiceList) {
		this.invoiceList = invoiceList;
	}
	

}
