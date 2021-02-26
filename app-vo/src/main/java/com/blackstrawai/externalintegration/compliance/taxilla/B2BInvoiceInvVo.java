package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class B2BInvoiceInvVo {
	private String chksum;
	private String updby;
	private String inum;
	private String idt;
	private double val;
	private String pos;
	private String rchrg;
	private String etin;
	private String invTyp;
	private String cflag;
	private double diffPercent;
	private String opd;
	private List<B2BInvoiceInvItmVo> itms;
	private String oinum;
	private String oidt;
	private String flag;
	
	public String getChksum() {
		return chksum;
	}
	public void setChksum(String chksum) {
		this.chksum = chksum;
	}
	public String getUpdby() {
		return updby;
	}
	public void setUpdby(String updby) {
		this.updby = updby;
	}
	public String getInum() {
		return inum;
	}
	public void setInum(String inum) {
		this.inum = inum;
	}
	public String getIdt() {
		return idt;
	}
	public void setIdt(String idt) {
		this.idt = idt;
	}
	public double getVal() {
		return val;
	}
	public void setVal(double val) {
		this.val = val;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getRchrg() {
		return rchrg;
	}
	public void setRchrg(String rchrg) {
		this.rchrg = rchrg;
	}
	public String getEtin() {
		return etin;
	}
	public void setEtin(String etin) {
		this.etin = etin;
	}
	public String getInvTyp() {
		return invTyp;
	}
	public void setInvTyp(String invTyp) {
		this.invTyp = invTyp;
	}
	public String getCflag() {
		return cflag;
	}
	public void setCflag(String cflag) {
		this.cflag = cflag;
	}
	public String getOpd() {
		return opd;
	}
	public void setOpd(String opd) {
		this.opd = opd;
	}
	public List<B2BInvoiceInvItmVo> getItms() {
		return itms;
	}
	public void setItms(List<B2BInvoiceInvItmVo> itms) {
		this.itms = itms;
	}
	public double getDiffPercent() {
		return diffPercent;
	}
	public void setDiffPercent(double diffPercent) {
		this.diffPercent = diffPercent;
	}
	public String getOinum() {
		return oinum;
	}
	public void setOinum(String oinum) {
		this.oinum = oinum;
	}
	public String getOidt() {
		return oidt;
	}
	public void setOidt(String oidt) {
		this.oidt = oidt;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
