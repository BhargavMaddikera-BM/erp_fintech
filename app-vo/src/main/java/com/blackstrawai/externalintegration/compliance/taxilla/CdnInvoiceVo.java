package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class CdnInvoiceVo {
	private String ctin;
	private String cfs;
	private List<CdnInvoiceNtVo> nt;
	public String getCtin() {
		return ctin;
	}
	public void setCtin(String ctin) {
		this.ctin = ctin;
	}
	public String getCfs() {
		return cfs;
	}
	public void setCfs(String cfs) {
		this.cfs = cfs;
	}
	public List<CdnInvoiceNtVo> getNt() {
		return nt;
	}
	public void setNt(List<CdnInvoiceNtVo> nt) {
		this.nt = nt;
	}
	
	
}
