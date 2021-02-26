package com.blackstrawai.export;

import java.sql.Date;
import java.util.List;

public class AccountingEntryExportVo {

	private Date date;
	private Integer id;
	private String type;
	private String journalNo;
	private String location;
	private String level1Email;
	private String level2Email;
	private String level3Email;
	private String status;
	List<AccountingEntryItemExportVo> accountingEntryItems;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLevel1Email() {
		return level1Email;
	}

	public void setLevel1Email(String level1Email) {
		this.level1Email = level1Email;
	}

	public String getLevel2Email() {
		return level2Email;
	}

	public void setLevel2Email(String level2Email) {
		this.level2Email = level2Email;
	}

	public String getLevel3Email() {
		return level3Email;
	}

	public void setLevel3Email(String level3Email) {
		this.level3Email = level3Email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AccountingEntryItemExportVo> getAccountingEntryItems() {
		return accountingEntryItems;
	}

	public void setAccountingEntryItems(List<AccountingEntryItemExportVo> accountingEntryItems) {
		this.accountingEntryItems = accountingEntryItems;
	}

}
