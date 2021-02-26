package com.blackstrawai.request.externalintegration.banking.common;

import com.blackstrawai.common.BaseRequest;

public class BankStatementRequest extends BaseRequest {
  private String serviceName;
  private String reqRefNo;
  private String reqRefTimeStamp;
  private String customerId;
  private String accountNo;
  private String transactionStartDate;
  private String transactionEndDate;
  private String orgId;
  private String accountName;
  private String type;
  private String roleName;
  private String period;
  private String searchParam;

  public String getSearchParam() {
    return searchParam;
  }

  public void setSearchParam(String searchParam) {
    this.searchParam = searchParam;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

 
  public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getReqRefNo() {
    return reqRefNo;
  }

  public void setReqRefNo(String reqRefNo) {
    this.reqRefNo = reqRefNo;
  }

  public String getReqRefTimeStamp() {
    return reqRefTimeStamp;
  }

  public void setReqRefTimeStamp(String reqRefTimeStamp) {
    this.reqRefTimeStamp = reqRefTimeStamp;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getTransactionStartDate() {
    return transactionStartDate;
  }

  public void setTransactionStartDate(String transactionStartDate) {
    this.transactionStartDate = transactionStartDate;
  }

  public String getTransactionEndDate() {
    return transactionEndDate;
  }

  public void setTransactionEndDate(String transactionEndDate) {
    this.transactionEndDate = transactionEndDate;
  }
  public String getAccountName() {
	return accountName;
}

public void setAccountName(String accountName) {
	this.accountName = accountName;
}

}
