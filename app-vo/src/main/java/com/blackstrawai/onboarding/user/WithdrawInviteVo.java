package com.blackstrawai.onboarding.user;

import com.blackstrawai.common.TokenVo;

public class WithdrawInviteVo extends TokenVo {

	private int id;
	private String reason;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
