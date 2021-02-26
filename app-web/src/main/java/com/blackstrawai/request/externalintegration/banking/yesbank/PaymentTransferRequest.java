package com.blackstrawai.request.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseRequest;

public class PaymentTransferRequest extends BaseRequest {

  private String customerId;
  private String accountNumber;
  private String paymentOptionId;
  private boolean accountDetails;
  private String orgId;
  private String paymentId;
  private String roleName;
  private String isSuperAdmin;
  private PaymentDetailsRequest singlePayment;

  public PaymentDetailsRequest getSinglePayment() {
    return singlePayment;
  }

  public void setSinglePayment(PaymentDetailsRequest singlePayment) {
    this.singlePayment = singlePayment;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getPaymentOptionId() {
    return paymentOptionId;
  }

  public void setPaymentOptionId(String paymentOptionId) {
    this.paymentOptionId = paymentOptionId;
  }

  public boolean isAccountDetails() {
    return accountDetails;
  }

  public void setAccountDetails(boolean accountDetails) {
    this.accountDetails = accountDetails;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getIsSuperAdmin() {
    return isSuperAdmin;
  }

  public void setIsSuperAdmin(String isSuperAdmin) {
    this.isSuperAdmin = isSuperAdmin;
  }
}
