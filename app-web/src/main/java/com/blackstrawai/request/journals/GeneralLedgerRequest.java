package com.blackstrawai.request.journals;

import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.payroll.payrun.PayRunVo;

public class GeneralLedgerRequest extends BaseRequest{

	
	private ArInvoiceVo invoice;

	private CreditNotesVo creditNote;
	
	private PaymentNonCoreVo payments;
	
	private PayRunVo payrun;
	
	private ReceiptVo receiptVo;
	
	private InvoiceVo apInvoice;
	
	
	public InvoiceVo getApInvoice() {
		return apInvoice;
	}

	public void setApInvoice(InvoiceVo apInvoice) {
		this.apInvoice = apInvoice;
	}

	public ReceiptVo getReceiptVo() {
		return receiptVo;
	}

	public void setReceiptVo(ReceiptVo receiptVo) {
		this.receiptVo = receiptVo;
	}

	public PayRunVo getPayrun() {
		return payrun;
	}

	public void setPayrun(PayRunVo payrun) {
		this.payrun = payrun;
	}

	public ArInvoiceVo getInvoice() {
		return invoice;
	}

	public void setInvoice(ArInvoiceVo invoice) {
		this.invoice = invoice;
	}

	public CreditNotesVo getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(CreditNotesVo creditNote) {
		this.creditNote = creditNote;
	}

	public PaymentNonCoreVo getPayments() {
		return payments;
	}

	public void setPayments(PaymentNonCoreVo payments) {
		this.payments = payments;
	}

	
	
}
