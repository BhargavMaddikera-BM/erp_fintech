package com.blackstrawai.externalintegration.compliance.taxilla;

public class B2csInvoiceVo {
	private String flag;
	private String chksum;
	private String splyTy;
	private double diffPercent;
	private Integer rt;
	private String typ;
	private String etin;
	private String pos;
	private Integer txval;
	private Integer iamt;
	private Integer csamt;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getChksum() {
		return chksum;
	}
	public void setChksum(String chksum) {
		this.chksum = chksum;
	}
	public String getSplyTy() {
		return splyTy;
	}
	public void setSplyTy(String splyTy) {
		this.splyTy = splyTy;
	}
	public double getDiffPercent() {
		return diffPercent;
	}
	public void setDiffPercent(double diffPercent) {
		this.diffPercent = diffPercent;
	}
	public Integer getRt() {
		return rt;
	}
	public void setRt(Integer rt) {
		this.rt = rt;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getEtin() {
		return etin;
	}
	public void setEtin(String etin) {
		this.etin = etin;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public Integer getTxval() {
		return txval;
	}
	public void setTxval(Integer txval) {
		this.txval = txval;
	}
	public Integer getIamt() {
		return iamt;
	}
	public void setIamt(Integer iamt) {
		this.iamt = iamt;
	}
	public Integer getCsamt() {
		return csamt;
	}
	public void setCsamt(Integer csamt) {
		this.csamt = csamt;
	}
	
	
	
}
