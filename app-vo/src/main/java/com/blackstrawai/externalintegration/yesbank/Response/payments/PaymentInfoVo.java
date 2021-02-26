package com.blackstrawai.externalintegration.yesbank.Response.payments;

public class PaymentInfoVo {
  private String payingTo;
  private String beneficiaryName;
  private String bankAccountName;
  private String accountNo;
  private String IFSCCode;
  private double amount;
  private String paymentMode;

  public String getPayingTo() {
    return payingTo;
  }

  public void setPayingTo(String payingTo) {
    this.payingTo = payingTo;
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

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }
}
