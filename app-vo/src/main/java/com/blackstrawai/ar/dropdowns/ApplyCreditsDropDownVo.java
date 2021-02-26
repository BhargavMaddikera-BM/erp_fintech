package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ar.applycredits.CreditNotesDetailsVo;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.applycredits.ReceiptsDetailsVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ApplyCreditsDropDownVo {

	private List<MinimalChartOfAccountsVo> ledger;
	private List<InvoiceDetailsVo> invoice;
	private List<ReceiptsDetailsVo> receiptDetails;
	private List<CreditNotesDetailsVo> creditNoesDetails;
	private String advanceBalance;
	private String creditNotesBalance;

	public List<ReceiptsDetailsVo> getReceiptDetails() {
		return receiptDetails;
	}

	public void setReceiptDetails(List<ReceiptsDetailsVo> receiptDetails) {
		this.receiptDetails = receiptDetails;
	}

	public List<CreditNotesDetailsVo> getCreditNoesDetails() {
		return creditNoesDetails;
	}

	public void setCreditNoesDetails(List<CreditNotesDetailsVo> creditNoesDetails) {
		this.creditNoesDetails = creditNoesDetails;
	}

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

	public List<MinimalChartOfAccountsVo> getLedger() {
		return ledger;
	}

	public void setLedger(List<MinimalChartOfAccountsVo> ledger) {
		this.ledger = ledger;
	}

	public List<InvoiceDetailsVo> getInvoice() {
		return invoice;
	}

	public void setInvoice(List<InvoiceDetailsVo> invoice) {
		this.invoice = invoice;
	}

}
