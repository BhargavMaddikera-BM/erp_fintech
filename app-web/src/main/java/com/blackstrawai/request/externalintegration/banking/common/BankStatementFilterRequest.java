package com.blackstrawai.request.externalintegration.banking.common;

import com.blackstrawai.common.BaseRequest;

public class BankStatementFilterRequest extends BaseRequest {
  private String customerId;
  private String accountNo;
  private String startDate;
  private String endDate;
  private String orgId;
  private String accountName;
  private String type;
  private String roleName;
  private boolean isAll;
  private boolean isCr;
  private boolean isDr;
  private int periodId;

  public int getPeriodId() {
    return periodId;
  }

  public void setPeriodId(int periodId) {
    this.periodId = periodId;
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

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
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

  public boolean isAll() {
    return isAll;
  }

  public void setIsAll(boolean isAll) {
    this.isAll = isAll;
  }

  public boolean isCr() {
    return isCr;
  }

  public void setIsCr(boolean isCr) {
    this.isCr = isCr;
  }

  public boolean isDr() {
    return isDr;
  }

  public void setIsDr(boolean isDr) {
    this.isDr = isDr;
  }
}
