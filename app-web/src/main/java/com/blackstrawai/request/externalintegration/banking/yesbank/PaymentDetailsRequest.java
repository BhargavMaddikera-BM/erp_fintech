package com.blackstrawai.request.externalintegration.banking.yesbank;

public class PaymentDetailsRequest {

  private String paidTo;
  private String beneficiaryName;
  private String amount;
  private String paymentMode;
  private String billRef;
  private String paymentRef;
  private String bankAccountName;
  private String beneficiaryId;
  private String beneficiaryType;
  private String beneficiaryAccountNumber;
  private String ifscCode;
  private String emailId;
  private String mobileNo;
  private String description;

  public String getPaidTo() {
    return paidTo;
  }

  public void setPaidTo(String paidTo) {
    this.paidTo = paidTo;
  }

  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }

  public String getBillRef() {
    return billRef;
  }

  public void setBillRef(String billRef) {
    this.billRef = billRef;
  }

  public String getPaymentRef() {
    return paymentRef;
  }

  public void setPaymentRef(String paymentRef) {
    this.paymentRef = paymentRef;
  }

  public String getBankAccountName() {
    return bankAccountName;
  }

  public void setBankAccountName(String bankAccountName) {
    this.bankAccountName = bankAccountName;
  }

  public String getBeneficiaryId() {
    return beneficiaryId;
  }

  public void setBeneficiaryId(String beneficiaryId) {
    this.beneficiaryId = beneficiaryId;
  }

  public String getBeneficiaryType() {
    return beneficiaryType;
  }

  public void setBeneficiaryType(String beneficiaryType) {
    this.beneficiaryType = beneficiaryType;
  }

  public String getBeneficiaryAccountNumber() {
    return beneficiaryAccountNumber;
  }

  public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
    this.beneficiaryAccountNumber = beneficiaryAccountNumber;
  }

  public String getIfscCode() {
    return ifscCode;
  }

  public void setIfscCode(String ifscCode) {
    this.ifscCode = ifscCode;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
