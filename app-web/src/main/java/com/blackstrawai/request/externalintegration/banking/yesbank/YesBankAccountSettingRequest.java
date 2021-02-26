package com.blackstrawai.request.externalintegration.banking.yesbank;

public class YesBankAccountSettingRequest extends YesBankCustomerInformationRequest{
	private boolean isApiBankingEnabled;
	private boolean isPaymentAllowed;
	private Integer erpBankAccountId;
	private String ifsc;

	
	public boolean getIsApiBankingEnabled() {
		return isApiBankingEnabled;
	}

	public void setIsApiBankingEnabled(boolean isApiBankingEnabled) {
		this.isApiBankingEnabled = isApiBankingEnabled;
	}

	public String getIfsc() {
		return ifsc;
	}

	public boolean getIsPaymentAllowed() {
		return isPaymentAllowed;
	}

	public Integer getErpBankAccountId() {
		return erpBankAccountId;
	}

	
	
	
}
