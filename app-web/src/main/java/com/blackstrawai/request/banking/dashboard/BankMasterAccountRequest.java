package com.blackstrawai.request.banking.dashboard;

import java.sql.Date;

import com.blackstrawai.common.BaseRequest;

public class BankMasterAccountRequest extends BaseRequest {

	private String accountName;
	private Integer accountType;
	private Integer accountVariant;
	private Date openingDate;
	private String accountNumber;
	private String ifscCode;
	private String bankName;
	private String branchName;
	private Integer accountCurrencyId;
	private String currentBalance="0";
	private String interestRate;
	private Integer termYear;
	private Integer termMonth;
	private Date maturityDate;
	private String limit;
	private String accountCode;
	private Integer id;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private String roleName;
	
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getAccountVariant() {
		return accountVariant;
	}

	public void setAccountVariant(Integer accountVariant) {
		this.accountVariant = accountVariant;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getAccountCurrencyId() {
		return accountCurrencyId;
	}

	public void setAccountCurrencyId(Integer accountCurrencyId) {
		this.accountCurrencyId = accountCurrencyId;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTermYear() {
		return termYear;
	}

	public void setTermYear(Integer termYear) {
		this.termYear = termYear;
	}

	public Integer getTermMonth() {
		return termMonth;
	}

	public void setTermMonth(Integer termMonth) {
		this.termMonth = termMonth;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

}
