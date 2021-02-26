package com.blackstrawai.ar.receipt;
public class ReceiptSettingsVo{
	
	private String ledgerId;	
	private String ledgerName;
	private String cName;
	private boolean isColumnShow;
	private String cType;
	
	

	public String getcType() {
		return cType;
	}
	public void setcType(String cType) {
		this.cType = cType;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public boolean isColumnShow() {
		return isColumnShow;
	}
	public void setColumnShow(boolean isColumnShow) {
		this.isColumnShow = isColumnShow;
	}
	
	

}
