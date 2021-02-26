package com.blackstrawai.externalintegration.yesbank.dropdowns;

public class PaymentBeneficiaryListVo {

  private Integer contactId;
  private String beneficiaryType;
  private String beneficiaryName;
  private String bankAccountName;
  private String accountNo;
  private String IFSCCode;
  private String mobileNo;
  private String emailId;
  private Integer bankId;
  private String type;
  private String status;
  private PaymentBillReferenceVo paymentBillReference;

  public Integer getContactId() {
    return contactId;
  }

  public void setContactId(Integer contactId) {
    this.contactId = contactId;
  }

  public String getBeneficiaryType() {
    return beneficiaryType;
  }

  public void setBeneficiaryType(String beneficiaryType) {
    this.beneficiaryType = beneficiaryType;
  }

  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public String getBankAccountName() {
    return bankAccountName;
  }

  public void setBankAccountName(String bankAccountName) {
    this.bankAccountName = bankAccountName;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getIFSCCode() {
    return IFSCCode;
  }

  public void setIFSCCode(String IFSCCode) {
    this.IFSCCode = IFSCCode;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public Integer getBankId() {
    return bankId;
  }

  public void setBankId(Integer bankId) {
    this.bankId = bankId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public PaymentBillReferenceVo getPaymentBillReference() {
    return paymentBillReference;
  }

  public void setPaymentBillReference(PaymentBillReferenceVo paymentBillReference) {
    this.paymentBillReference = paymentBillReference;
  }
}
