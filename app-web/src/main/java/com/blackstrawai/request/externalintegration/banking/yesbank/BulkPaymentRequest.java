package com.blackstrawai.request.externalintegration.banking.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class BulkPaymentRequest extends BaseRequest{

	private Integer debitAccountId;
	
	private String debitAccount;
	
	private String  paymentDescription;
	
    private String customerId;

    private int orgId;

    private String roleName;

    private String controSum;
    
	private List<BulkPaymentDetailsRequest> paymentDetails;
	
	public String getControSum() {
		return controSum;
	}

	public void setControSum(String controSum) {
		this.controSum = controSum;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getDebitAccountId() {
		return debitAccountId;
	}

	public void setDebitAccountId(Integer debitAccountId) {
		this.debitAccountId = debitAccountId;
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public List<BulkPaymentDetailsRequest> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<BulkPaymentDetailsRequest> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	@Override
	public String toString() {
		return "BulkPaymentRequest [debitAccountId=" + debitAccountId + ", debitAccount=" + debitAccount
				+ ", paymentDescription=" + paymentDescription + ", customerId=" + customerId + ", orgId=" + orgId
				+ ", roleName=" + roleName + ", controSum=" + controSum + ", paymentDetails=" + paymentDetails + "]";
	}
	
	
	


}
