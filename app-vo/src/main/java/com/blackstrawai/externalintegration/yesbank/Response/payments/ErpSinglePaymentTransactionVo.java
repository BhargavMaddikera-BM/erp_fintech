package com.blackstrawai.externalintegration.yesbank.Response.payments;

import java.sql.Date;
import java.sql.Timestamp;

public class ErpSinglePaymentTransactionVo {
	private String customerId;
	  private String accountNumber;
	  private String paymentOptionId;
	  private boolean accountDetails;
	  private String orgId;
	  private String paymentId;
	  private String roleName;
	  private String isSuperAdmin;
	  private ErpSinglePaymentDetailsVo singlePayment;
	  private String userId;
	  private Date createTs;
	  private Timestamp updateTs;
	  
	  
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	public ErpSinglePaymentDetailsVo getSinglePayment() {
		return singlePayment;
	}
	public void setSinglePayment(ErpSinglePaymentDetailsVo singlePayment) {
		this.singlePayment = singlePayment;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public Date getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}

}
