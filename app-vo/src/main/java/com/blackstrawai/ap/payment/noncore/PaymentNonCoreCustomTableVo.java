package com.blackstrawai.ap.payment.noncore;

public class PaymentNonCoreCustomTableVo {
	private String ledgerName;
	private String ledgerId;
	private String cName;
	private boolean columnShow;
	private String colName;
	
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public boolean isColumnShow() {
		return columnShow;
	}
	public void setColumnShow(boolean columnShow) {
		this.columnShow = columnShow;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	
}
