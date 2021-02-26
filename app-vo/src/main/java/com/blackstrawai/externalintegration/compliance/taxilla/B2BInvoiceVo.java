package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class B2BInvoiceVo {
	private String ctin;
	private String cfs;
	private List<B2BInvoiceInvVo> inv;
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
	public List<B2BInvoiceInvVo> getInv() {
		return inv;
	}
	public void setInv(List<B2BInvoiceInvVo> inv) {
		this.inv = inv;
	}
	
	
}
