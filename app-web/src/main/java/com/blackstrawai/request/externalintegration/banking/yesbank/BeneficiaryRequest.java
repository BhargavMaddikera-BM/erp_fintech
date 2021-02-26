package com.blackstrawai.request.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseRequest;

import java.sql.Date;

public class BeneficiaryRequest extends BaseRequest {

  private Integer id;
  private String beneficiaryType;
  private String beneficiaryName;
  private String accountHolderName;
  private String accountNumber;
  private String confirmAccountNumber;
  private String accountType;
  private String ifscCode;
  private String upiId;
  private String branchName;
  private String bankName;
  private Integer organizationId;
  private String status;
  private String roleName;
  private Boolean isDefault;
  private String accountName;
  private Integer accountVariant;
  private Date openingDate;
  private Integer accountCurrencyId;
  private String currentBalance;
  private String interestRate;
  private Integer termYear;
  private Integer termMonth;
  private Date maturityDate;
  private String limit;
  private String accountCode;
  private Boolean isSuperAdmin;
  private String type;
  private String mobileNo;
  private String email;

  public String getMobileNo() {
	return mobileNo;
}

public String getEmail() {
	return email;
}

public Boolean getDefault() {
    return isDefault;
  }

  public String getAccountName() {
    return accountName;
  }

  public Integer getAccountVariant() {
    return accountVariant;
  }

  public Date getOpeningDate() {
    return openingDate;
  }

  public Integer getAccountCurrencyId() {
    return accountCurrencyId;
  }

  public String getCurrentBalance() {
    return currentBalance;
  }

  public String getInterestRate() {
    return interestRate;
  }

  public Integer getTermYear() {
    return termYear;
  }

  public Integer getTermMonth() {
    return termMonth;
  }

  public Date getMaturityDate() {
    return maturityDate;
  }

  public String getLimit() {
    return limit;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public Boolean getIsSuperAdmin() {
    return isSuperAdmin;
  }
  // private String status;
  // private String roleName;

  public Integer getId() {
    return id;
  }

  public String getBeneficiaryType() {
    return beneficiaryType;
  }

  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getConfirmAccountNumber() {
    return confirmAccountNumber;
  }

  public String getAccountType() {
    return accountType;
  }

  public String getIfscCode() {
    return ifscCode;
  }

  public String getUpiId() {
    return upiId;
  }

  public String getBranchName() {
    return branchName;
  }

  public String getBankName() {
    return bankName;
  }

  public Integer getOrganizationId() {
    return organizationId;
  }

  public String getStatus() {
    return status;
  }

  public String getAccountHolderName() {
    return accountHolderName;
  }

  public String getRoleName() {
    return roleName;
  }

  public Boolean getIsDefault() {
    return isDefault;
  }

  public String getType() {
    return type;
  }
}
