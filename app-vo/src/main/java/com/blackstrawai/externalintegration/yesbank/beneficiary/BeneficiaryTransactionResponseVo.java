package com.blackstrawai.externalintegration.yesbank.beneficiary;

public class BeneficiaryTransactionResponseVo {

  private String id;
  private String transactionDate;
  private String transactionNumber;
  private String beneficiaryName;
  private String beneficiaryType;
  private String amount;
  private String status;
  private int noOfTxn ;
  
  
  public int getNoOfTxn() {
	return noOfTxn;
}

public void setNoOfTxn(int noOfTxn) {
	this.noOfTxn = noOfTxn;
}

public String getResponseJson() {
    return responseJson;
  }

  public void setResponseJson(String responseJson) {
    this.responseJson = responseJson;
  }

  private String responseJson;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(String transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getTransactionNumber() {
    return transactionNumber;
  }

  public void setTransactionNumber(String transactionNumber) {
    this.transactionNumber = transactionNumber;
  }

  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public String getBeneficiaryType() {
    return beneficiaryType;
  }

  public void setBeneficiaryType(String beneficiaryType) {
    this.beneficiaryType = beneficiaryType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
