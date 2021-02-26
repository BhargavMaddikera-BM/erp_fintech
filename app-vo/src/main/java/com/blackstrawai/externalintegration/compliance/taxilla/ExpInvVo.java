package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class ExpInvVo {
	private String flag;
	private String chksum;
	private String inum;
	private String idt;
	private double val;
	private String sbpcode;
	private String sbnum;
	private String sbdt;
	private double diffPercent;
	private List<B2BInvoiceInvItmDetVo> itms;
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
	public String getSbpcode() {
		return sbpcode;
	}
	public void setSbpcode(String sbpcode) {
		this.sbpcode = sbpcode;
	}
	public String getSbnum() {
		return sbnum;
	}
	public void setSbnum(String sbnum) {
		this.sbnum = sbnum;
	}
	public String getSbdt() {
		return sbdt;
	}
	public void setSbdt(String sbdt) {
		this.sbdt = sbdt;
	}
	public double getDiffPercent() {
		return diffPercent;
	}
	public void setDiffPercent(double diffPercent) {
		this.diffPercent = diffPercent;
	}
	public List<B2BInvoiceInvItmDetVo> getItms() {
		return itms;
	}
	public void setItms(List<B2BInvoiceInvItmDetVo> itms) {
		this.itms = itms;
	}
	
	
}
