package com.blackstrawai.ap.creditnote;

public class ApplyFundVo {
	
	private int invoiceId;
	private double appliedAmount;

	public int getInvoiceId() {
		return invoiceId;
	}

	public double getAppliedAmount() {
		return appliedAmount;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setAppliedAmount(double appliedAmount) {
		this.appliedAmount = appliedAmount;
	}

	@Override
	public String toString() {
		return "ApplyFundCreateRequest [invoiceId=" + invoiceId + ", appliedAmount=" + appliedAmount + "]";
	}

}
