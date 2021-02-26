package com.blackstrawai.settings.chartofaccounts;

import com.blackstrawai.common.BaseVo;

public class ChartOfAccountsLedgerDetailsVo extends BaseVo{
	// Do not Change this order 
		private String level1Name;
		
		private String level2Name;
		
		private String level3Name;
		
		private String level4Name;
		
		private String ledgerName;
		
		private Integer ledgerId;
		
		private String ledgerStatus;
		
		private Boolean isBase;

		public String getLevel1Name() {
			return level1Name;
		}

		public void setLevel1Name(String level1Name) {
			this.level1Name = level1Name;
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

		public String getLedgerName() {
			return ledgerName;
		}

		public void setLedgerName(String ledgerName) {
			this.ledgerName = ledgerName;
		}

		public Integer getLedgerId() {
			return ledgerId;
		}

		public void setLedgerId(Integer ledgerId) {
			this.ledgerId = ledgerId;
		}

		public String getLedgerStatus() {
			return ledgerStatus;
		}

		public void setLedgerStatus(String ledgerStatus) {
			this.ledgerStatus = ledgerStatus;
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
			builder.append("ChartOfAccountsLedgerDetailsVo [level1Name=");
			builder.append(level1Name);
			builder.append(", level2Name=");
			builder.append(level2Name);
			builder.append(", level3Name=");
			builder.append(level3Name);
			builder.append(", level4Name=");
			builder.append(level4Name);
			builder.append(", ledgerName=");
			builder.append(ledgerName);
			builder.append(", ledgerId=");
			builder.append(ledgerId);
			builder.append(", ledgerStatus=");
			builder.append(ledgerStatus);
			builder.append(", isBase=");
			builder.append(isBase);
			builder.append("]");
			return builder.toString();
		}
		
		
}
