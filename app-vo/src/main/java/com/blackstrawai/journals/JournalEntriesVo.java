package com.blackstrawai.journals;

import java.sql.Date;

import com.blackstrawai.common.BaseVo;

public class JournalEntriesVo extends BaseVo{

	
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
	
	private Integer customerId;
	
	private String creditNoteNo;
	
	private String customerInvoiceNo;
	
	private String customerPoNo;
	
	private Integer vendorId;
	
	private String debitNoteNo;
	
	private String vendorInvoiceNo;
	
	private String vendorPoNo;
	
	private Integer bankId;
	
	private String gstLevel;
	
	private String gstPercentage;
	
	private Integer tdsId;
	
	private Integer employeeId;
	
	private String costCenter;
	
	private String billingBasis;
	
	private Integer locationId;
	
	private String gst;
	
	private Double amountDebitOriginalCurrrency;
	
	private Double amountCreditOriginalCurrency;
	
	private Integer currency;
	
	private Double conversionFactor;
	
	private Double amountDebit;
	
	private Double amountCredit;
	
	private String GstInputCerdit;
	
	private String nature;
	
	private String remarks;
	
	private Integer orgId;
	
	private Boolean isSuperAdmin;
	
	private String bankType;
	
	private String roleName;
	
	private String ledgerId;
	
	private String voucherNaration;
	
	private String reciptType;
	
	
	public String getReciptType() {
		return reciptType;
	}

	public void setReciptType(String reciptType) {
		this.reciptType = reciptType;
	}

	public String getVoucherNaration() {
		return voucherNaration;
	}

	public void setVoucherNaration(String voucherNaration) {
		this.voucherNaration = voucherNaration;
	}

	public String getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
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

	public Integer getTdsId() {
		return tdsId;
	}

	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
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

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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
