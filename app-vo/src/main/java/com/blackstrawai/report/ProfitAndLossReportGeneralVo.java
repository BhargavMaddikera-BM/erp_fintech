package com.blackstrawai.report;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProfitAndLossReportGeneralVo {
	
	private String levelType;
	private String level2Name;
	private String level3Name;
	private String level4Name;
	private String level5Name;
	private String ledgerId;
	private String journalBookName;
	private BigDecimal OpeningBalance;
	private BigDecimal ClosingBalance;
	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
	public String getLevel2Name() {
		return level2Name;
	}
	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}
	public String getLevel3Name() {
		return level3Name;
	}
	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
	}
	public String getLevel4Name() {
		return level4Name;
	}
	public void setLevel4Name(String level4Name) {
		this.level4Name = level4Name;
	}
	public String getLevel5Name() {
		return level5Name;
	}
	public void setLevel5Name(String level5Name) {
		this.level5Name = level5Name;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getJournalBookName() {
		return journalBookName;
	}
	public void setJournalBookName(String journalBookName) {
		this.journalBookName = journalBookName;
	}
	public BigDecimal getOpeningBalance() {
		
		return (OpeningBalance != null)? OpeningBalance.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
		
	}
	public void setOpeningBalance(BigDecimal openingBalance) {
		OpeningBalance = openingBalance;
	}
	public BigDecimal getClosingBalance() {
		
		return (ClosingBalance != null)? ClosingBalance.setScale(2,RoundingMode.CEILING):BigDecimal.ZERO;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		ClosingBalance = closingBalance;
	}
	@Override
	public String toString() {
		return "ProfitAndLossGeneralVo [levelType=" + levelType + ", level2Name=" + level2Name + ", level3Name="
				+ level3Name + ", level4Name=" + level4Name + ", level5Name=" + level5Name + ", journalBookName="
				+ journalBookName + ", OpeningBalance=" + OpeningBalance + ", ClosingBalance=" + ClosingBalance + "]";
	}
	
	 
  
}
