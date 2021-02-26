package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentInitiationVo {

	@JsonProperty("InstructionIdentification")
	private String instructionIdentification ;
	
	@JsonProperty("ClearingSystemIdentification")
	private String clearingSystemIdentification ;
	
	@JsonProperty("InstructedAmount")
	private BulkPaymentInstructedAmountVo instructedAmount ;
	
	@JsonProperty("DebtorAccount")
	private BulkPaymentDebtorAccountVo debtorAccount ;
	
	@JsonProperty("CreditorAccount")
	private BulkPaymentCreditorAccountVo creditorAccount ;
	
	@JsonProperty("RemittanceInformation")
	private BulkPaymentRemittanceInformationVo remittanceInformation ;

	public String getInstructionIdentification() {
		return instructionIdentification;
	}

	public void setInstructionIdentification(String instructionIdentification) {
		this.instructionIdentification = instructionIdentification;
	}

	public String getClearingSystemIdentification() {
		return clearingSystemIdentification;
	}

	public void setClearingSystemIdentification(String clearingSystemIdentification) {
		this.clearingSystemIdentification = clearingSystemIdentification;
	}

	public BulkPaymentInstructedAmountVo getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(BulkPaymentInstructedAmountVo instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public BulkPaymentDebtorAccountVo getDebtorAccount() {
		return debtorAccount;
	}

	public void setDebtorAccount(BulkPaymentDebtorAccountVo debtorAccount) {
		this.debtorAccount = debtorAccount;
	}

	public BulkPaymentCreditorAccountVo getCreditorAccount() {
		return creditorAccount;
	}

	public void setCreditorAccount(BulkPaymentCreditorAccountVo creditorAccount) {
		this.creditorAccount = creditorAccount;
	}

	public BulkPaymentRemittanceInformationVo getRemittanceInformation() {
		return remittanceInformation;
	}

	public void setRemittanceInformation(BulkPaymentRemittanceInformationVo remittanceInformation) {
		this.remittanceInformation = remittanceInformation;
	}
	
	
}
