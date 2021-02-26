package com.blackstrawai.externalintegration.compliance.taxilla;

import java.util.List;

public class ExpVo {
	private String expTyp;
	private List<ExpInvVo> inv;
	public String getExpTyp() {
		return expTyp;
	}
	public void setExpTyp(String expTyp) {
		this.expTyp = expTyp;
	}
	public List<ExpInvVo> getInv() {
		return inv;
	}
	public void setInv(List<ExpInvVo> inv) {
		this.inv = inv;
	}
	
	
}
