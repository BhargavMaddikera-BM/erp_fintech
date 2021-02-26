package com.blackstrawai.ar.invoice;

import java.util.List;

import com.blackstrawai.ar.receipt.BasicReceiptVo;
import com.blackstrawai.common.BaseVo;

public class ArInvoiceListVo extends BaseVo{

	private Integer invoiceId;
	
	private String customerName;
	
	private String invoiceNumber;
	
	private String poNumber;
	
	private String invoiceDate;
	
	private String dueDate;
	
	private String status;
	
	private Double total;

	private Double balanceDue;
	
	private String currencySymbol;
	
	private Integer currencyId;
	
	private Integer customerId;
	
	private List<BasicReceiptVo> receipts;
	
	private Double creditNoteTotal;
	
	private String pendingApprovalStatus;
	
	
	
	public String getPendingApprovalStatus() {
		return pendingApprovalStatus;
	}

	public void setPendingApprovalStatus(String pendingApprovalStatus) {
		this.pendingApprovalStatus = pendingApprovalStatus;
	}

	public Double getCreditNoteTotal() {
		return creditNoteTotal;
	}

	public void setCreditNoteTotal(Double creditNoteTotal) {
		this.creditNoteTotal = creditNoteTotal;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<BasicReceiptVo> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<BasicReceiptVo> receipts) {
		this.receipts = receipts;
	}
	
	
}
