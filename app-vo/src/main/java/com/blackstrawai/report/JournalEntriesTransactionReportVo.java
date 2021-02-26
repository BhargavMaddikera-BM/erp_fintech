package com.blackstrawai.report;

import java.sql.Date;

import com.blackstrawai.common.TokenVo;

public class JournalEntriesTransactionReportVo extends TokenVo {

private Integer id;
	
	private Integer transactionNo;
	
	private Integer originalTransactionNo;
	
	private Integer  transactionLineNo;
	
	private String module;
	
	private String subModule;
	
	private String voucherType;
	
	private String voucherNo;
	
	private Date dateOfEntry;
	
	private Date effectiveDate;
	
	private Date invoiceDate;
	
	private String particulars;
	
	private String customerName;
	
	private String creditNoteNo;
	
	private String customerInvoiceNo;
	
	private String customerPoNo;
	
	private String vendorName;
	
	private String debitNoteNo;
	
	private String vendorInvoiceNo;
	
	private String vendorPoNo;
	
	private String bankDetails;
	
	private String gstLevel;
	
	private String gstPercentage;
	
	private String tdsDetails;
	
	private String employeeName;
	
	private String costCenter;
	
	private String billingBasis;
	
	private String location;
	
	private String gst;
	
	private Double amountDebitOriginalCurrrency;
	
	private Double amountCreditOriginalCurrency;
	
	private String currency;
	
	private Double conversionFactor;
	
	private Double amountDebit;
	
	private Double amountCredit;
	
	private String GstInputCerdit;
	
	private String nature;
	
	private String remarks;

	private String userName;
	private Boolean isSuperAdmin;
	
	private String bankType;
	
	private String status;

	
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(Integer transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Integer getOriginalTransactionNo() {
		return originalTransactionNo;
	}

	public void setOriginalTransactionNo(Integer originalTransactionNo) {
		this.originalTransactionNo = originalTransactionNo;
	}

	public Integer getTransactionLineNo() {
		return transactionLineNo;
	}

	public void setTransactionLineNo(Integer transactionLineNo) {
		this.transactionLineNo = transactionLineNo;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getDateOfEntry() {
		return dateOfEntry;
	}

	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public String getCustomerInvoiceNo() {
		return customerInvoiceNo;
	}

	public void setCustomerInvoiceNo(String customerInvoiceNo) {
		this.customerInvoiceNo = customerInvoiceNo;
	}

	public String getCustomerPoNo() {
		return customerPoNo;
	}

	public void setCustomerPoNo(String customerPoNo) {
		this.customerPoNo = customerPoNo;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDebitNoteNo() {
		return debitNoteNo;
	}

	public void setDebitNoteNo(String debitNoteNo) {
		this.debitNoteNo = debitNoteNo;
	}

	public String getVendorInvoiceNo() {
		return vendorInvoiceNo;
	}

	public void setVendorInvoiceNo(String vendorInvoiceNo) {
		this.vendorInvoiceNo = vendorInvoiceNo;
	}

	public String getVendorPoNo() {
		return vendorPoNo;
	}

	public void setVendorPoNo(String vendorPoNo) {
		this.vendorPoNo = vendorPoNo;
	}

	public String getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}

	public String getGstLevel() {
		return gstLevel;
	}

	public void setGstLevel(String gstLevel) {
		this.gstLevel = gstLevel;
	}

	public String getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(String gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public String getTdsDetails() {
		return tdsDetails;
	}

	public void setTdsDetails(String tdsDetails) {
		this.tdsDetails = tdsDetails;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getBillingBasis() {
		return billingBasis;
	}

	public void setBillingBasis(String billingBasis) {
		this.billingBasis = billingBasis;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public Double getAmountDebitOriginalCurrrency() {
		return amountDebitOriginalCurrrency;
	}

	public void setAmountDebitOriginalCurrrency(Double amountDebitOriginalCurrrency) {
		this.amountDebitOriginalCurrrency = amountDebitOriginalCurrrency;
	}

	public Double getAmountCreditOriginalCurrency() {
		return amountCreditOriginalCurrency;
	}

	public void setAmountCreditOriginalCurrency(Double amountCreditOriginalCurrency) {
		this.amountCreditOriginalCurrency = amountCreditOriginalCurrency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getConversionFactor() {
		return conversionFactor;
	}

	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	public Double getAmountDebit() {
		return amountDebit;
	}

	public void setAmountDebit(Double amountDebit) {
		this.amountDebit = amountDebit;
	}

	public Double getAmountCredit() {
		return amountCredit;
	}

	public void setAmountCredit(Double amountCredit) {
		this.amountCredit = amountCredit;
	}

	public String getGstInputCerdit() {
		return GstInputCerdit;
	}

	public void setGstInputCerdit(String gstInputCerdit) {
		GstInputCerdit = gstInputCerdit;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	

}
