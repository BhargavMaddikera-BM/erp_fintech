package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class B2clInvoiceVo {
	private String pos;
	private List<B2BInvoiceInvVo> inv;
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public List<B2BInvoiceInvVo> getInv() {
		return inv;
	}
	public void setInv(List<B2BInvoiceInvVo> inv) {
		this.inv = inv;
	}
	
	
}
