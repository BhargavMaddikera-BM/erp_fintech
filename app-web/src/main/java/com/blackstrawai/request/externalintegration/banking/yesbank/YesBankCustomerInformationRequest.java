package com.blackstrawai.request.externalintegration.banking.yesbank;

public class YesBankCustomerInformationRequest {
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
	

	public Integer getId() {
		return id;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public String getUserId() {
		return userId;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getPassword() {
		return password;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getUserName() {
		return userName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getAuthKey1() {
		return authKey1;
	}

	public String getAuthKey2() {
		return authKey2;
	}

	public String getEcollectCode() {
		return ecollectCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getBranch() {
		return branch;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	
}
