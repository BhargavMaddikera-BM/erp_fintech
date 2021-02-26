package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class NilRatedSuppliesVo {
	private String flag;
	private String chksum;
	private List<NilRatedSuppliesInvVo> inv;
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
	public List<NilRatedSuppliesInvVo> getInv() {
		return inv;
	}
	public void setInv(List<NilRatedSuppliesInvVo> inv) {
		this.inv = inv;
	}
	
	
}
