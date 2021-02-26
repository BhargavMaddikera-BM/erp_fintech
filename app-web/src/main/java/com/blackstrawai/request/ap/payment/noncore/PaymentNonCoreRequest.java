package com.blackstrawai.request.ap.payment.noncore;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ap.payment.noncore.CreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreCustomTableVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentNonCoreRequest extends BaseRequest {
	private Integer id;
	private String userId;
	private int organizationId;
	private int paidVia;
	private int paymentType;
	private String paymentRefNo;
	private String paymentDate;
	private int vendor;
	private int vendorAccountId;
	private String vendorAccountName;
	private int currency;
	private String currencySymbol;
	private String amountPaid;
	private String notes;
//	private List<BillDetailsVo> billDetails;
	private List<CreditDetailsVo> creditList;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private String poRefNo;
	private String paidTo;
	private int contactAccountId;
	private String contactAccountName;
	private int contactId;
	private int payType;
	private String payPeriod;
	@JsonProperty
	private boolean isSuperAdmin;
	private String billAmount;
	private String adjustedAmount;
	private String differenceAmount;
	private String totalAmount;
	private List<PaymentNonCoreCustomTableVo> customTableList;
	private String status;
	private String bankFees;
	private String dueAmount;
	private String tdsDeductions;
	private String payRunRefNo;
	private String others1;
	private String others2;
	private String others3;
	private String tax;
	private String interest;
	private String penalty;
	@JsonProperty
	private boolean isRecord;
	private List<Integer> itemsToRemove = new ArrayList<Integer>();
	private List<Integer> itemsToRemoveCredit = new ArrayList<Integer>();
	private List<PaymentNonCoreBaseVo> payments;
	private String roleName;
	private int paymentMode;
	private String contactType;
	private int creditAccountId;
	private int statutoryBody;
	
	private int customerName;
	private int paidFor;
	private int employeeName;
	private String currencyCode;
	private String exchangeRate;
	
	private int billRef;
	private int referenceId;
	private int invoiceId;
	private String referenceType;
	@JsonProperty
	private boolean isBulk;
	
	private String paidViaName;
	private String contactName;
	private String invRefName;
	private String baseCurrencyCode;
	private GeneralLedgerVo generalLedgerData;
	private String displayDueAmount;
	private String bankReferenceNumber;
	
	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}
	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}
	public String getPaidViaName() {
		return paidViaName;
	}
	public void setPaidViaName(String paidViaName) {
		this.paidViaName = paidViaName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getInvRefName() {
		return invRefName;
	}
	public void setInvRefName(String invRefName) {
		this.invRefName = invRefName;
	}
	public int getBillRef() {
		return billRef;
	}
	public void setBillRef(int billRef) {
		this.billRef = billRef;
	}
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public boolean isBulk() {
		return isBulk;
	}
	public void setBulk(boolean isBulk) {
		this.isBulk = isBulk;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public int getCustomerName() {
		return customerName;
	}
	public void setCustomerName(int customerName) {
		this.customerName = customerName;
	}
	public int getPaidFor() {
		return paidFor;
	}
	public void setPaidFor(int paidFor) {
		this.paidFor = paidFor;
	}
	public int getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(int employeeName) {
		this.employeeName = employeeName;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getPaidVia() {
		return paidVia;
	}
	public void setPaidVia(int paidVia) {
		this.paidVia = paidVia;
	}
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentRefNo() {
		return paymentRefNo;
	}
	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public int getVendor() {
		return vendor;
	}
	public void setVendor(int vendor) {
		this.vendor = vendor;
	}
	public int getVendorAccountId() {
		return vendorAccountId;
	}
	public void setVendorAccountId(int vendorAccountId) {
		this.vendorAccountId = vendorAccountId;
	}
	public String getVendorAccountName() {
		return vendorAccountName;
	}
	public void setVendorAccountName(String vendorAccountName) {
		this.vendorAccountName = vendorAccountName;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public List<CreditDetailsVo> getCreditList() {
		return creditList;
	}
	public void setCreditList(List<CreditDetailsVo> creditList) {
		this.creditList = creditList;
	}
	public List<UploadFileVo> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}
	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}
	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}
	public String getPoRefNo() {
		return poRefNo;
	}
	public void setPoRefNo(String poRefNo) {
		this.poRefNo = poRefNo;
	}
	public String getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}
	public int getContactAccountId() {
		return contactAccountId;
	}
	public void setContactAccountId(int contactAccountId) {
		this.contactAccountId = contactAccountId;
	}
	public String getContactAccountName() {
		return contactAccountName;
	}
	public void setContactAccountName(String contactAccountName) {
		this.contactAccountName = contactAccountName;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getAdjustedAmount() {
		return adjustedAmount;
	}
	public void setAdjustedAmount(String adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}
	public String getDifferenceAmount() {
		return differenceAmount;
	}
	public void setDifferenceAmount(String differenceAmount) {
		this.differenceAmount = differenceAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<PaymentNonCoreCustomTableVo> getCustomTableList() {
		return customTableList;
	}
	public void setCustomTableList(List<PaymentNonCoreCustomTableVo> customTableList) {
		this.customTableList = customTableList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBankFees() {
		return bankFees;
	}
	public void setBankFees(String bankFeesType) {
		this.bankFees = bankFeesType;
	}
	public String getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(String dueAmountType) {
		this.dueAmount = dueAmountType;
	}
	public String getTdsDeductions() {
		return tdsDeductions;
	}
	public void setTdsDeductions(String tdsDeductionsType) {
		this.tdsDeductions = tdsDeductionsType;
	}
	public String getPayRunRefNo() {
		return payRunRefNo;
	}
	public void setPayRunRefNo(String payRunRefNo) {
		this.payRunRefNo = payRunRefNo;
	}
	public String getOthers1() {
		return others1;
	}
	public void setOthers1(String others1) {
		this.others1 = others1;
	}
	public String getOthers2() {
		return others2;
	}
	public void setOthers2(String others2) {
		this.others2 = others2;
	}
	public String getOthers3() {
		return others3;
	}
	public void setOthers3(String others3) {
		this.others3 = others3;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String taxType) {
		this.tax = taxType;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interestType) {
		this.interest = interestType;
	}
	public String getPenalty() {
		return penalty;
	}
	public void setPenalty(String penaltyType) {
		this.penalty = penaltyType;
	}
	public boolean isRecord() {
		return isRecord;
	}
	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}
	public List<Integer> getItemsToRemove() {
		return itemsToRemove;
	}
	public void setItemsToRemove(List<Integer> itemsToRemove) {
		this.itemsToRemove = itemsToRemove;
	}
	public List<PaymentNonCoreBaseVo> getPayments() {
		return payments;
	}
	public void setPayments(List<PaymentNonCoreBaseVo> payments) {
		this.payments = payments;
	}
	public int getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public int getCreditAccountId() {
		return creditAccountId;
	}
	public void setCreditAccountId(int creditAccountId) {
		this.creditAccountId = creditAccountId;
	}
	public List<Integer> getItemsToRemoveCredit() {
		return itemsToRemoveCredit;
	}
	public void setItemsToRemoveCredit(List<Integer> itemsToRemoveCredit) {
		this.itemsToRemoveCredit = itemsToRemoveCredit;
	}
	public int getStatutoryBody() {
		return statutoryBody;
	}
	public void setStatutoryBody(int statutoryBody) {
		this.statutoryBody = statutoryBody;
	}
	public String getBaseCurrencyCode() {
		return baseCurrencyCode;
	}
	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}
	public String getDisplayDueAmount() {
		return displayDueAmount;
	}
	public void setDisplayDueAmount(String displayDueAmount) {
		this.displayDueAmount = displayDueAmount;
	}
	public String getBankReferenceNumber() {
		return bankReferenceNumber;
	}
	public void setBankReferenceNumber(String bankReferenceNumber) {
		this.bankReferenceNumber = bankReferenceNumber;
	}
	
	
	
}
