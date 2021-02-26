package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentRemittanceUnstructuredVo {

  @JsonProperty("CreditorReferenceInformation")
  private String creditorReferenceInformation;
  
  @JsonProperty("RemitterAccount")
  private String remitterAccount;

  public void setCreditorReferenceInformation(String creditorReferenceInformation) {
    this.creditorReferenceInformation = creditorReferenceInformation;
  }

public void setRemitterAccount(String remitterAccount) {
	this.remitterAccount = remitterAccount;
}

public String getCreditorReferenceInformation() {
	return creditorReferenceInformation;
}

public String getRemitterAccount() {
	return remitterAccount;
}
  
  
}
