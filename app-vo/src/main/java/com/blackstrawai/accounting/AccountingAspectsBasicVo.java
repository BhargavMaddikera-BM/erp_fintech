package com.blackstrawai.accounting;

import java.sql.Date;

import com.blackstrawai.common.BaseVo;

public class AccountingAspectsBasicVo extends BaseVo {

	private Date doc;
	private String type;
	private String JournalNo;
	private String status;
	private String currency;
	private String amount;
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDoc() {
		return doc;
	}

	public void setDoc(Date doc) {
		this.doc = doc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJournalNo() {
		return JournalNo;
	}

	public void setJournalNo(String journalNo) {
		JournalNo = journalNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	
}
