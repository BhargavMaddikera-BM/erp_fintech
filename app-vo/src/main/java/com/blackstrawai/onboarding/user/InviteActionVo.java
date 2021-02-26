package com.blackstrawai.onboarding.user;

import com.blackstrawai.common.TokenVo;

public class InviteActionVo extends TokenVo {

	private int id;
	private String action;
	private String reason;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	

}
