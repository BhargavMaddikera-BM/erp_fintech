package com.blackstrawai.journals;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.common.BaseVo;

public class GeneralLedgerVo extends BaseVo{

private Integer moduleId;
	
List<GeneralLedgerDetailsVo> glDetails;

private String txnReferenceNo;

private String journalVoucherNo;

private String voucherNaration;

private Double exchangeRate;

private String currencySymbol;

private String orgCurrencySymbol ;

private Date journalDate;



public String getOrgCurrencySymbol() {
	return orgCurrencySymbol;
}

public void setOrgCurrencySymbol(String orgCurrencySymbol) {
	this.orgCurrencySymbol = orgCurrencySymbol;
}

public Date getJournalDate() {
	return journalDate;
}

public void setJournalDate(Date journalDate) {
	this.journalDate = journalDate;
}

public String getCurrencySymbol() {
	return currencySymbol;
}

public void setCurrencySymbol(String currencySymbol) {
	this.currencySymbol = currencySymbol;
}

public Integer getModuleId() {
	return moduleId;
}

public void setModuleId(Integer moduleId) {
	this.moduleId = moduleId;
}

public Double getExchangeRate() {
	return exchangeRate;
}

public void setExchangeRate(Double exchangeRate) {
	this.exchangeRate = exchangeRate;
}

public List<GeneralLedgerDetailsVo> getGlDetails() {
	return glDetails;
}

public void setGlDetails(List<GeneralLedgerDetailsVo> glDetails) {
	this.glDetails = glDetails;
}

public String getTxnReferenceNo() {
	return txnReferenceNo;
}

public void setTxnReferenceNo(String txnReferenceNo) {
	this.txnReferenceNo = txnReferenceNo;
}

public String getJournalVoucherNo() {
	return journalVoucherNo;
}

public void setJournalVoucherNo(String journalVoucherNo) {
	this.journalVoucherNo = journalVoucherNo;
}

public String getVoucherNaration() {
	return voucherNaration;
}

public void setVoucherNaration(String voucherNaration) {
	this.voucherNaration = voucherNaration;
}

@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("GeneralLedgerVo [moduleId=");
	builder.append(moduleId);
	builder.append(", glDetails=");
	builder.append(glDetails);
	builder.append(", txnReferenceNo=");
	builder.append(txnReferenceNo);
	builder.append(", journalVoucherNo=");
	builder.append(journalVoucherNo);
	builder.append(", voucherNaration=");
	builder.append(voucherNaration);
	builder.append(", exchangeRate=");
	builder.append(exchangeRate);
	builder.append(", currencySymbol=");
	builder.append(currencySymbol);
	builder.append("]");
	return builder.toString();
}



	

}
