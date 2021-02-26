package com.blackstrawai.report;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReportLedgerDataVo {
	
		private String dataId ;
		private String particulars ;
		private String effectiveDate ;
		private String vendorInvoiceNumber ;
	    private String vendorPONumber ;
	    private String voucherNumber;
	    private String customerInvoiceNumber ;
	    private String customerPONumber ;
	    private String creditNoteNumber ;
	    private String debitNoteNumber ;
	    private String Type ;
	    private String accountCode ;
		private BigDecimal OpeningBalance;
		private BigDecimal periodBalance;
		private BigDecimal creditAmount;
		private BigDecimal debitAmount;
		public String getDataId() {
			return dataId;
		}
		public void setDataId(String dataId) {
			this.dataId = dataId;
		}
		public String getParticulars() {
			return particulars;
		}
		public void setParticulars(String particulars) {
			this.particulars = particulars;
		}
		public String getEffectiveDate() {
			return effectiveDate;
		}
		public void setEffectiveDate(String effectiveDate) {
			this.effectiveDate = effectiveDate;
		}
		public String getVendorInvoiceNumber() {
			return vendorInvoiceNumber;
		}
		public void setVendorInvoiceNumber(String vendorInvoiceNumber) {
			this.vendorInvoiceNumber = vendorInvoiceNumber;
		}
		public String getVendorPONumber() {
			return vendorPONumber;
		}
		public void setVendorPONumber(String vendorPONumber) {
			this.vendorPONumber = vendorPONumber;
		}
		public String getVoucherNumber() {
			return voucherNumber;
		}
		public void setVoucherNumber(String voucherNumber) {
			this.voucherNumber = voucherNumber;
		}
		public String getCustomerInvoiceNumber() {
			return customerInvoiceNumber;
		}
		public void setCustomerInvoiceNumber(String customerInvoiceNumber) {
			this.customerInvoiceNumber = customerInvoiceNumber;
		}
		public String getCustomerPONumber() {
			return customerPONumber;
		}
		public void setCustomerPONumber(String customerPONumber) {
			this.customerPONumber = customerPONumber;
		}
		public String getCreditNoteNumber() {
			return creditNoteNumber;
		}
		public void setCreditNoteNumber(String creditNoteNumber) {
			this.creditNoteNumber = creditNoteNumber;
		}
		public String getDebitNoteNumber() {
			return debitNoteNumber;
		}
		public void setDebitNoteNumber(String debitNoteNumber) {
			this.debitNoteNumber = debitNoteNumber;
		}
		public String getType() {
			return Type;
		}
		public void setType(String type) {
			Type = type;
		}
		public String getAccountCode() {
			return accountCode;
		}
		public void setAccountCode(String accountCode) {
			this.accountCode = accountCode;
		}
		public BigDecimal getOpeningBalance() {
			return (OpeningBalance != null)? OpeningBalance.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
		}
		public void setOpeningBalance(BigDecimal openingBalance) {
			OpeningBalance = openingBalance;
		}
		public BigDecimal getPeriodBalance() {
			return (periodBalance != null)? periodBalance.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
		}
		public void setPeriodBalance(BigDecimal closingBalance) {
			periodBalance = closingBalance;
		}
		public BigDecimal getCreditAmount() {
			return (creditAmount != null)? creditAmount.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
		}
		public void setCreditAmount(BigDecimal creditAmount) {
			this.creditAmount = creditAmount;
		}
		public BigDecimal getDebitAmount() {
			return (debitAmount != null)? debitAmount.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
		}
		public void setDebitAmount(BigDecimal debitAmount) {
			this.debitAmount = debitAmount;
		}
		
		
	    

}
