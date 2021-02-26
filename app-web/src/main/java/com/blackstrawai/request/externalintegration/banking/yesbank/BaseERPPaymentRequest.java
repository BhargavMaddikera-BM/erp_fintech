package com.blackstrawai.request.externalintegration.banking.yesbank;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.journals.GeneralLedgerVo;

public class BaseERPPaymentRequest extends BaseRequest{

private String fileIdentifier;

private String debitAccountNo;

private Integer debitAccountId;

private String customerId;

private Date transactionDate;

private String prefixNo;

private String voucherNo;

private String suffixNo;

private String paymentDescription;

private String moduleName;

private List<PaymentNonCoreVo> generalLedgers;

private String totalAmount;

private Integer orgId;

private String roleName;

private String status;

private Integer currencyId;

public Integer getCurrencyId() {
	return currencyId;
}

public void setCurrencyId(Integer currencyId) {
	this.currencyId = currencyId;
}

private List<ERPPaymentDetailRequest> paymentDetails;

public List<ERPPaymentDetailRequest> getPaymentDetails() {
	return paymentDetails;
}

public void setPaymentDetails(List<ERPPaymentDetailRequest> paymentDetails) {
	this.paymentDetails = paymentDetails;
}

public String getFileIdentifier() {
	return fileIdentifier;
}

public void setFileIdentifier(String fileIdentifier) {
	this.fileIdentifier = fileIdentifier;
}

public String getDebitAccountNo() {
	return debitAccountNo;
}

public void setDebitAccountNo(String debitAccountNo) {
	this.debitAccountNo = debitAccountNo;
}

public Integer getDebitAccountId() {
	return debitAccountId;
}

public void setDebitAccountId(Integer debitAccountId) {
	this.debitAccountId = debitAccountId;
}

public String getCustomerId() {
	return customerId;
}

public void setCustomerId(String customerId) {
	this.customerId = customerId;
}

public Date getTransactionDate() {
	return transactionDate;
}

public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
}

public String getPrefixNo() {
	return prefixNo;
}

public void setPrefixNo(String prefixNo) {
	this.prefixNo = prefixNo;
}

public String getVoucherNo() {
	return voucherNo;
}

public void setVoucherNo(String voucherNo) {
	this.voucherNo = voucherNo;
}

public String getSuffixNo() {
	return suffixNo;
}

public void setSuffixNo(String suffixNo) {
	this.suffixNo = suffixNo;
}

public String getPaymentDescription() {
	return paymentDescription;
}

public void setPaymentDescription(String paymentDescription) {
	this.paymentDescription = paymentDescription;
}

public String getModuleName() {
	return moduleName;
}

public void setModuleName(String moduleName) {
	this.moduleName = moduleName;
}



public List<PaymentNonCoreVo> getGeneralLedgers() {
	return generalLedgers;
}

public void setGeneralLedgers(List<PaymentNonCoreVo> generalLedgers) {
	this.generalLedgers = generalLedgers;
}

public String getTotalAmount() {
	return totalAmount;
}

public void setTotalAmount(String totalAmount) {
	this.totalAmount = totalAmount;
}

public Integer getOrgId() {
	return orgId;
}

public void setOrgId(Integer orgId) {
	this.orgId = orgId;
}

public String getRoleName() {
	return roleName;
}

public void setRoleName(String roleName) {
	this.roleName = roleName;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}



}
