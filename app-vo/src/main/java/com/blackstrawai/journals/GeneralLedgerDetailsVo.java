package com.blackstrawai.journals;

import java.util.List;

import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class GeneralLedgerDetailsVo {

	
	private String description;
	
	private String type;
	
	private Integer productId;

	private Integer tempId;
	
	private String account;

	private Integer accountId;

	private String subLedger;

	private Double fcyDebit;

	private Double fcyCredit;

	private Double inrDebit;

	private Double inrCredit;

	private List<MinimalChartOfAccountsVo> ledgerList;

	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(String subLedger) {
		this.subLedger = subLedger;
	}

	public Double getFcyDebit() {
		return fcyDebit;
	}

	public void setFcyDebit(Double fcyDebit) {
		this.fcyDebit = fcyDebit;
	}

	public Double getFcyCredit() {
		return fcyCredit;
	}

	public void setFcyCredit(Double fcyCredit) {
		this.fcyCredit = fcyCredit;
	}

	public Double getInrDebit() {
		return inrDebit;
	}

	public void setInrDebit(Double inrDebit) {
		this.inrDebit = inrDebit;
	}

	public Double getInrCredit() {
		return inrCredit;
	}

	public void setInrCredit(Double inrCredit) {
		this.inrCredit = inrCredit;
	}

	public List<MinimalChartOfAccountsVo> getLedgerList() {
		return ledgerList;
	}

	public void setLedgerList(List<MinimalChartOfAccountsVo> ledgerList) {
		this.ledgerList = ledgerList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneralLedgerDetailsVo [description=");
		builder.append(description);
		builder.append(", type=");
		builder.append(type);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", tempId=");
		builder.append(tempId);
		builder.append(", account=");
		builder.append(account);
		builder.append(", accountId=");
		builder.append(accountId);
		builder.append(", subLedger=");
		builder.append(subLedger);
		builder.append(", fcyDebit=");
		builder.append(fcyDebit);
		builder.append(", fcyCredit=");
		builder.append(fcyCredit);
		builder.append(", inrDebit=");
		builder.append(inrDebit);
		builder.append(", inrCredit=");
		builder.append(inrCredit);
		builder.append(", ledgerList=");
		builder.append(ledgerList);
		builder.append("]");
		return builder.toString();
	}
	
	
	

}
