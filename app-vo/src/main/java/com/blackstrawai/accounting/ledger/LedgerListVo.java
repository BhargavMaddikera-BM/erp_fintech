package com.blackstrawai.accounting.ledger;

import java.util.List;

import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel6Vo;

public class LedgerListVo {

	// Do not Change this order 
	private String level1Name;
	
	private String level2Name;
	
	private String level3Name;
	
	private String level4Name;
	
	private String ledgerName;
	
	private Integer ledgerId;
	
	private String ledgerStatus;
	
	private Boolean isBase;
	
	private List<ChartOfAccountsLevel6Vo> subledgers;

	public Integer getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Integer ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public String getLedgerStatus() {
		return ledgerStatus;
	}

	public void setLedgerStatus(String ledgerStatus) {
		this.ledgerStatus = ledgerStatus;
	}

	public String getLevel4Name() {
		return level4Name;
	}

	public void setLevel4Name(String level4Name) {
		this.level4Name = level4Name;
	}

	public String getLevel3Name() {
		return level3Name;
	}

	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
	}

	public String getLevel2Name() {
		return level2Name;
	}

	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}

	public String getLevel1Name() {
		return level1Name;
	}

	public void setLevel1Name(String level1Name) {
		this.level1Name = level1Name;
	}

	public List<ChartOfAccountsLevel6Vo> getSubledgers() {
		return subledgers;
	}

	public void setSubledgers(List<ChartOfAccountsLevel6Vo> subledgers) {
		this.subledgers = subledgers;
	}

	
	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LedgerListVo [ledgerId=");
		builder.append(ledgerId);
		builder.append(", ledgerName=");
		builder.append(ledgerName);
		builder.append(", ledgerStatus=");
		builder.append(ledgerStatus);
		builder.append(", level4Name=");
		builder.append(level4Name);
		builder.append(", level3Name=");
		builder.append(level3Name);
		builder.append(", level2Name=");
		builder.append(level2Name);
		builder.append(", level1Name=");
		builder.append(level1Name);
		builder.append(", subledgers=");
		builder.append(subledgers);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
