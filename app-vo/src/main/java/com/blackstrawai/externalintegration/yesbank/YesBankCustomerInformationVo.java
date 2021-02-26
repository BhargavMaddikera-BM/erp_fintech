package com.blackstrawai.externalintegration.yesbank;

public class YesBankCustomerInformationVo {
	
	private Integer id;
	private String customerId;
	private String userName;
	private String accountNo;
	private String authKey1;
	private String authKey2;
	private String ecollectCode;
	private String accountName;
	private String branch;
	private String mobileNo;
	private String password;
	private Integer orgId;
	private String userId;
	private String roleName;
	private boolean isApiBankingEnabled;
	private boolean isPaymentAllowed;
	private Integer erpBankAccountId; 
	private String ifsc;
	
	
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public boolean getIsPaymentAllowed() {
		return isPaymentAllowed;
	}
	public void setIsPaymentAllowed(boolean isPaymentAllowed) {
		this.isPaymentAllowed = isPaymentAllowed;
	}
	
	public Integer getErpBankAccountId() {
		return erpBankAccountId;
	}
	public void setErpBankAccountId(Integer erpBankAccountId) {
		this.erpBankAccountId = erpBankAccountId;
	}
	public boolean getIsApiBankingEnabled() {
		return isApiBankingEnabled;
	}
	public void setIsApiBankingEnabled(boolean isApiBankingEnabled) {
		this.isApiBankingEnabled = isApiBankingEnabled;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAuthKey1() {
		return authKey1;
	}
	public void setAuthKey1(String authKey1) {
		this.authKey1 = authKey1;
	}
	public String getAuthKey2() {
		return authKey2;
	}
	public void setAuthKey2(String authKey2) {
		this.authKey2 = authKey2;
	}
	public String getEcollectCode() {
		return ecollectCode;
	}
	public void setEcollectCode(String ecollectCode) {
		this.ecollectCode = ecollectCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	
	
}
